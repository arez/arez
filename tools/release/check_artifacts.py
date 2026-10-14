#!/usr/bin/env python3
"""Verify published archive contents, source completeness and bytecode baselines."""

import argparse
import hashlib
import json
import zipfile
from pathlib import Path


ARTIFACTS = {
    "arez-core": ("core", "arez/Arez.class"),
    "arez-processor": ("processor", "arez/processor/ArezProcessor.class"),
    "arez-extras-dom": ("extras/dom", "arez/dom/BrowserLocation.class"),
    "arez-extras-promise": ("extras/promise", "arez/promise/ObservablePromise.class"),
    "arez-extras-spytools": ("extras/spytools", "arez/spytools/SpyUtil.class"),
    "arez-extras-testng": ("extras/testng", "arez/testng/ArezTestSupport.class"),
    "arez-persist-core": ("persist/core", "arez/persist/runtime/ArezPersist.class"),
    "arez-persist-processor": ("persist/processor", "arez/persist/processor/ArezPersistProcessor.class"),
}
NATIVE_SOURCES = {
    "arez-core": ["arez/ArezConfig.native.js", "arez/arez.js"],
    "arez-persist-core": ["arez/persist/runtime/ArezPersistConfig.native.js", "arez/persist/runtime/arez.persist.js"],
}
PROCESSORS = {
    "arez-processor": "arez.processor.ArezProcessor",
    "arez-persist-processor": "arez.persist.processor.ArezPersistProcessor",
}


def require(condition: bool, message: str) -> None:
    if not condition:
        raise SystemExit(message)


def archive(path: Path) -> dict[str, bytes]:
    with zipfile.ZipFile(path) as jar:
        names = jar.namelist()
        require(len(names) == len(set(names)), f"Duplicate entries: {path.name}")
        require(names[0] == "META-INF/MANIFEST.MF" and names[1:] == sorted(names[1:]),
                f"Entries must follow the manifest in sorted order: {path.name}")
        for entry in jar.infolist():
            require(entry.date_time == (1980, 1, 1, 0, 0, 0), f"Unstable timestamp: {path.name}: {entry.filename}")
            require(not entry.is_dir(), f"Unexpected directory entry: {path.name}: {entry.filename}")
        result = {name: jar.read(name) for name in names}
    require(result["META-INF/MANIFEST.MF"] == b"Manifest-Version: 1.0\r\n\r\n", f"Stamped manifest: {path.name}")
    require("META-INF/LICENSE" in result, f"Missing license: {path.name}")
    require(not any(name.endswith(".gwt.xml") or name.startswith("META-INF/versions/") for name in names),
            f"Obsolete GWT or multi-release payload: {path.name}")
    return result


def check_archives(workspace: Path, root: Path) -> dict:
    evidence = {}
    for artifact, (module, sentinel) in ARTIFACTS.items():
        binary = archive(root / f"{artifact}.jar")
        sources = archive(root / f"{artifact}-sources.jar")
        require(sentinel in binary, f"Missing product class: {artifact}: {sentinel}")
        require(not any(name.endswith(".java") for name in binary), f"Java sources in binary: {artifact}")
        require(not any(name.endswith(".class") for name in sources), f"Bytecode in sources: {artifact}")
        authored = workspace / module / "src/main/java"
        for source in authored.rglob("*.java"):
            name = source.relative_to(authored).as_posix()
            require(sources.get(name) == source.read_bytes(), f"Missing or changed authored source: {artifact}: {name}")
        for name in NATIVE_SOURCES.get(artifact, []):
            require(name in sources, f"Missing native companion: {artifact}: {name}")
        vendor = PROCESSORS.get(artifact, "").rsplit(".", 1)[0].replace(".", "/") + "/vendor/"
        owned, shaded = 0, 0
        for name, data in binary.items():
            if not name.endswith(".class"):
                continue
            require(data[:4] == b"\xca\xfe\xba\xbe", f"Invalid class: {artifact}: {name}")
            major = int.from_bytes(data[6:8], "big")
            if artifact in PROCESSORS and name.startswith(vendor):
                require(major <= 65, f"Shaded dependency requires newer than Java 21: {artifact}: {name}: {major}")
                shaded += 1
            else:
                require(major == 61, f"Owned class requires Java 17 bytecode: {artifact}: {name}: {major}")
                require(name.startswith("arez/"), f"Unexpected unrelocated dependency: {artifact}: {name}")
                source = name.split("$", 1)[0].removesuffix(".class") + ".java"
                require(source in sources, f"Missing generated/authored source for owned class: {artifact}: {name}")
                owned += 1
        require(owned > 0, f"Empty binary: {artifact}")
        if artifact in PROCESSORS:
            require(shaded > 0 and vendor + "javapoet/JavaFile.class" in binary, f"Missing shaded Javapoet: {artifact}")
            require(any(name.startswith(vendor + "proton/") for name in binary), f"Missing shaded Proton: {artifact}")
            forbidden = ("com/palantir/javapoet/", "org/realityforge/proton/", "org/jspecify/", "javax/annotation/",
                         vendor + "proton/vendor/jackson/", vendor + "proton/vendor/google/errorprone/",
                         vendor + "proton/vendor/google/j2objc/", vendor + "proton/vendor/jspecify/")
            require(not any(name.startswith(forbidden) for name in binary), f"Unwanted processor dependency: {artifact}")
            service = "META-INF/services/javax.annotation.processing.Processor"
            require(binary.get(service, b"").decode().splitlines() == [PROCESSORS[artifact]],
                    f"Wrong processor service: {artifact}")
            require(sources.get(service) == binary[service], f"Missing processor service in sources: {artifact}")
            for name, data in binary.items():
                if name.startswith("META-INF/services/"):
                    for line in data.decode().splitlines():
                        provider = line.split("#", 1)[0].strip()
                        require(not provider or provider.replace(".", "/") + ".class" in binary,
                                f"Missing shaded service provider: {artifact}: {provider}")
        evidence[artifact] = {"owned_java_17_classes": owned, "shaded_java_21_compatible_classes": shaded,
                              "source_entries": len(sources), "binary_sha256": hashlib.sha256((root / f"{artifact}.jar").read_bytes()).hexdigest(),
                              "sources_sha256": hashlib.sha256((root / f"{artifact}-sources.jar").read_bytes()).hexdigest()}
    return evidence


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--directory", type=Path)
    parser.add_argument("--compare", type=Path, help="Independently built artifact directory")
    parser.add_argument("--evidence", type=Path)
    args = parser.parse_args()
    workspace = Path(__file__).resolve().parents[2]
    root = args.directory or workspace / "bazel-bin/tools/release"
    evidence = check_archives(workspace, root)
    if args.compare:
        other = check_archives(workspace, args.compare)
        require(evidence == other, "Independent archive builds differ")
    if args.evidence:
        args.evidence.write_text(json.dumps(evidence, indent=2) + "\n")
    print("Checked 16 complete binary/source archives, scoped bytecode baselines and processor shading")


if __name__ == "__main__":
    main()
