#!/usr/bin/env python3
"""Verify published archive contents, source completeness and bytecode baselines."""

import argparse
import hashlib
import json
import re
import os
import subprocess
import tempfile
import textwrap
import zipfile
import xml.etree.ElementTree as ET
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


def check_archives(workspace: Path, root: Path, version: str) -> dict:
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
        javadoc_path = root / f"{artifact}-javadoc.jar"
        docs = archive(javadoc_path)
        api_page = sentinel.removesuffix(".class") + ".html"
        require("index.html" in docs and api_page in docs, f"Incomplete API docs: {artifact}")
        for name, data in docs.items():
            if name.endswith(".html"):
                require(not re.search(rb"<!-- Generated by javadoc [^\r\n]* on ", data),
                        f"Javadoc timestamp: {artifact}: {name}")
        pom_path = root / f"{artifact}.pom"
        pom = ET.parse(pom_path).getroot()
        ns = {"m": "http://maven.apache.org/POM/4.0.0"}
        for field, expected in [("groupId", "org.realityforge.arez"), ("artifactId", artifact),
                                ("version", version), ("packaging", "jar")]:
            require(pom.findtext("m:" + field, namespaces=ns) == expected, f"Wrong {field}: {artifact}")
        require(pom.findtext("m:licenses/m:license/m:name", namespaces=ns)
                == "The Apache Software License, Version 2.0", f"Missing license: {artifact} POM")
        require(pom.findtext("m:url", namespaces=ns) == "https://github.com/arez/arez", f"Wrong project URL: {artifact}")
        dependencies = {":".join(dependency.findtext("m:" + field, namespaces=ns) or ""
                                 for field in ["groupId", "artifactId", "version"])
                        for dependency in pom.findall("m:dependencies/m:dependency", ns)}
        require(not any("javax.annotation" in dep or "gwt" in dep.lower() for dep in dependencies),
                f"Obsolete direct dependency: {artifact}")
        if artifact in PROCESSORS:
            require(not dependencies, f"Processor POM must be self-contained: {artifact}")
        else:
            require("org.jspecify:jspecify:1.0.1" in dependencies, f"Missing JSpecify dependency: {artifact}")
            if artifact != "arez-core":
                require(f"org.realityforge.arez:arez-core:{version}" in dependencies,
                        f"Missing matching core dependency: {artifact}")
        if artifact == "arez-extras-testng":
            require("org.testng:testng:7.10.2" in dependencies, "Missing TestNG API dependency")
        if artifact in ["arez-core", "arez-extras-spytools"]:
            require("org.realityforge.org.jetbrains.annotations:org.jetbrains.annotations:1.7.0" in dependencies,
                    f"Missing unrelated Language/MagicConstant metadata dependency: {artifact}")
        evidence[artifact] = {"owned_java_17_classes": owned, "shaded_java_21_compatible_classes": shaded,
                              "source_entries": len(sources), "binary_sha256": hashlib.sha256((root / f"{artifact}.jar").read_bytes()).hexdigest(),
                              "sources_sha256": hashlib.sha256((root / f"{artifact}-sources.jar").read_bytes()).hexdigest(),
                              "javadoc_sha256": hashlib.sha256(javadoc_path.read_bytes()).hexdigest(),
                              "pom_sha256": hashlib.sha256(pom_path.read_bytes()).hexdigest(),
                              "pom_dependencies": sorted(dependencies)}
    return evidence


def run(command: list[str], workspace: Path) -> str:
    result = subprocess.run(command, cwd=workspace, text=True, capture_output=True)
    require(result.returncode == 0, f"Command failed: {' '.join(command)}\n{result.stdout}{result.stderr}")
    return result.stdout.strip()


def check_isolated_processors(workspace: Path, root: Path) -> None:
    execution_root = Path(run(["bazel", "info", "execution_root"], workspace))
    runtime = run(["bazel", "cquery", "config(@rules_java//toolchains:current_java_runtime, target)",
                   "--output=starlark", '--starlark:expr=providers(target)["ToolchainInfo"].java_runtime.java_home'], workspace)
    java_home = execution_root / runtime
    require(re.search(r'\b21[.]', run([str(java_home / "bin/java"), "--version"], workspace)) is not None,
            "Isolated processors must run on Java 21")
    dependencies = []
    for label in ["@jspecify//file", "@braincheck//:core",
                  "@grim_annotations//file", "@jsinterop_annotations//file",
                  "@org_jetbrains_annotations//file", "@base//file", "@testng//file"]:
        paths = run(["bazel", "cquery", label, "--output=files"], workspace).splitlines()
        require(len(paths) == 1, f"Expected one declared dependency jar: {label}")
        dependencies.append(execution_root / paths[0])
    with tempfile.TemporaryDirectory(prefix="arez-isolated-processors-") as directory:
        work = Path(directory)
        source = work / "Consumer.java"
        source.write_text(textwrap.dedent("""\
            package isolated;
            import arez.*;
            import arez.annotations.*;
            import arez.persist.*;
            import arez.persist.runtime.*;
            import arez.spytools.SpyUtil;
            import java.util.Map;
            import java.util.HashMap;
            import org.jspecify.annotations.*;
            @NullMarked
            public final class Consumer {
              private Consumer() {}
              public static void main(String[] args) {
                var memory = new MemoryStore();
                ArezPersist.registerStore("memory", memory);
                var model = new Arez_Model();
                Model_PersistSidecar.attach(ArezPersist.getRootScope(), model);
                var calls = new int[1];
                var observer = Arez.context().observer(() -> {
                  model.getValue();
                  calls[0]++;
                });
                if (calls[0] != 1) throw new AssertionError("initial observation");
                Arez.context().safeAction(() -> model.setValue("updated"));
                if (calls[0] != 2) throw new AssertionError("change propagation");
                if (!"updated".equals(memory.saved.get(ArezPersist.getRootScope()).get("Model")
                    .get("one").getData().get("value"))) throw new AssertionError("persistence");
                Arez.context().safeAction(() -> {
                  if (!"updated".equals(model.getValue())) throw new AssertionError("value");
                  if (!"updated".equals(model.echo(new String[] {"updated", null}).get(0)))
                    throw new AssertionError("compiled array/element contract");
                  model.setValue(null);
                });
                if (calls[0] != 3) throw new AssertionError("nullable propagation");
                if (memory.saved.get(ArezPersist.getRootScope()).get("Model").containsKey("one"))
                  throw new AssertionError("skip null persisted value");
                var position = new arez.dom.Position(1, null, null, 2, 3, null);
                if (position.getLatitude() != 2 || position.getAltitude() != null)
                  throw new AssertionError("DOM value API");
                if (new arez.testng.ArezTestSupport() {}.context() != Arez.context())
                  throw new AssertionError("TestNG support API");
                if (SpyUtil.getNestingDelta(arez.spy.ActionStartEvent.class) != SpyUtil.NestingDelta.INCREASE)
                  throw new AssertionError("spytools");
                if (arez.promise.ObservablePromise.State.PENDING.ordinal() != 0
                    || arez.dom.Position.class.getDeclaredMethods().length == 0
                    || arez.testng.ArezTestSupport.class.getDeclaredMethods().length == 0)
                  throw new AssertionError("packaged extras APIs");
                if (Disposable.isDisposed(ArezPersist.getRootScope())) throw new AssertionError("Persist runtime");
                if (Scope.class.getDeclaredMethods().length == 0) throw new AssertionError("Persist source");
                observer.dispose();
                Disposable.dispose(model);
                if (!Disposable.isDisposed(model)) throw new AssertionError("disposal");
              }
              private static final class MemoryStore implements StorageService {
                final Map<Scope, Map<String, Map<String, Entry>>> saved = new HashMap<>();
                public void dispose() {}
                public void scheduleCommit(SafeProcedure action) { action.call(); }
                public void restore(Map<Scope, Map<String, Map<String, Entry>>> state) { state.clear(); }
                public void commit(Map<Scope, Map<String, Map<String, Entry>>> state) {
                  saved.clear(); saved.putAll(state);
                }
                public Object encodeState(Map<String, @Nullable Object> state, TypeConverter converter) {
                  return state;
                }
                @SuppressWarnings("unchecked")
                public Map<String, @Nullable Object> decodeState(Object state, TypeConverter converter) {
                  return (Map<String, @Nullable Object>) state;
                }
              }
            }
            """))
        model = work / "Model.java"
        model.write_text(textwrap.dedent("""\
            package isolated;
            import arez.annotations.*;
            import arez.persist.PersistType;
            import org.jspecify.annotations.*;
            @NullMarked
            @ArezComponent(sting = Feature.DISABLE)
            @PersistType(defaultStore = "memory")
            abstract class Model extends Parent<@Nullable String> {
              @arez.persist.PersistId @ComponentId String getId() { return "one"; }
            }
            """))
        classes, generated = work / "classes", work / "generated"
        classes.mkdir()
        generated.mkdir()
        classpath = os.pathsep.join(str(p.resolve()) for p in
                                   [root / f"{artifact}.jar" for artifact in ARTIFACTS if artifact not in PROCESSORS] + dependencies + [classes])
        processorpath = os.pathsep.join(str((root / f"{name}.jar").resolve()) for name in PROCESSORS)
        exports = [f"-J--add-exports=jdk.compiler/com.sun.tools.javac.{name}=ALL-UNNAMED"
                   for name in ["api", "code", "file", "parser", "tree", "util"]]
        parent = work / "Parent.java"
        parent.write_text(textwrap.dedent("""\
            package isolated;
            import arez.annotations.*;
            import arez.persist.Persist;
            import java.util.List;
            import java.util.Collections;
            import java.util.Arrays;
            import org.jspecify.annotations.*;
            @NullMarked
            public abstract class Parent<T extends @Nullable Object> {
              @Persist @Observable public abstract T getValue();
              public abstract void setValue(T value);
              @Action public List<@Nullable String> echo(@Nullable String @Nullable [] values) {
                return values == null ? Collections.singletonList(null) : Arrays.asList(values);
              }
            }
            """))
        run([str(java_home / "bin/javac"), "--release", "17", "-parameters", "-Werror",
             "-Xlint:all,-processing,-serial", "-sourcepath", "", "-implicit:none",
             "-XDaddTypeAnnotationsToSymbol=true", "-proc:none", "-classpath", classpath,
             "-d", str(classes), str(parent)], workspace)
        run([str(java_home / "bin/javac"), "--release", "17", "-Werror", "-Xlint:all,-processing,-serial",
             "-XDaddTypeAnnotationsToSymbol=true", "-sourcepath", "", "-implicit:none", *exports, "-classpath", classpath,
             "-processorpath", processorpath, "-processor", ",".join(PROCESSORS.values()),
             "-Aarez.format_generated_source=true", "-Aarez.persist.format_generated_source=true",
             "-d", str(classes), "-s", str(generated), str(source), str(model)], workspace)
        names = {p.relative_to(generated).as_posix() for p in generated.rglob("*.java")}
        require("isolated/Arez_Model.java" in names
                and "isolated/Model_PersistSidecar.java" in names
                and "isolated/Arez_Model_PersistSidecar.java" in names,
                f"Both packaged processors must generate and compile complete outputs: {names}")
        generated_model = (generated / "isolated/Arez_Model.java").read_text()
        require("List<@Nullable String>" in generated_model
                and "@Nullable String @Nullable []" in generated_model
                and "@Nullable String getValue()" in generated_model,
                "Packaged processor lost compiled inherited/generic/array JSpecify contracts")
        run([str(java_home / "bin/java"), "-ea", "-Darez.environment=development",
             "-Dbraincheck.environment=development", "-Darez.persist.environment=development",
             "-classpath", classpath, "isolated.Consumer"], workspace)
        for path in classes.rglob("*.class"):
            require(int.from_bytes(path.read_bytes()[6:8], "big") == 61,
                    f"Isolated generated class must target Java 17: {path.name}")
    print("Compiled inherited JSpecify contracts with both isolated Java 21 processors and ran all packaged JVM APIs")


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--directory", type=Path)
    parser.add_argument("--version", default="0.0.0-SNAPSHOT")
    parser.add_argument("--compare", type=Path, help="Independently built artifact directory")
    parser.add_argument("--evidence", type=Path)
    parser.add_argument("--skip-processors", action="store_true", help="Only inspect archive/POM contents")
    args = parser.parse_args()
    workspace = Path(__file__).resolve().parents[2]
    root = args.directory or workspace / "bazel-bin/tools/release"
    evidence = check_archives(workspace, root, args.version)
    if args.compare:
        other = check_archives(workspace, args.compare, args.version)
        require(evidence == other, "Independent archive builds differ")
    site = archive(root / "arez-api-javadoc.jar")
    for _, sentinel in ARTIFACTS.values():
        require(sentinel.removesuffix(".class") + ".html" in site, "Aggregate API docs must include all eight APIs")
    if args.compare:
        require((root / "arez-api-javadoc.jar").read_bytes() == (args.compare / "arez-api-javadoc.jar").read_bytes(),
                "Independent aggregate API documentation differs")
    if not args.skip_processors:
        check_isolated_processors(workspace, root)
    if args.evidence:
        args.evidence.write_text(json.dumps(evidence, indent=2) + "\n")
    print("Checked 32 Maven artifacts, complete sources/API docs, POM dependencies, bytecode and shading")


if __name__ == "__main__":
    main()
