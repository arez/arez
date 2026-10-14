#!/usr/bin/env python3
"""Stage the Bazel release outputs as a Maven Central upload bundle."""

import argparse
import hashlib
import os
import re
import shutil
import subprocess
import zipfile
import xml.etree.ElementTree as ET

from pathlib import Path

from check_artifacts import ARTIFACTS


KINDS = ("jar", "sources", "javadoc", "pom")


def filename(artifact: str, kind: str, version: str) -> str:
    if kind == "jar":
        return f"{artifact}-{version}.jar"
    if kind == "pom":
        return f"{artifact}-{version}.pom"
    return f"{artifact}-{version}-{kind}.jar"


def sign(path: Path) -> Path:
    signature = Path(f"{path}.asc")
    command = ["gpg", "--batch", "--yes", "--armor", "--detach-sign", "--output", str(signature)]
    if key := os.environ.get("GPG_USER"):
        command.extend(("--local-user", key))
    if passphrase := os.environ.get("GPG_PASS"):
        command.extend(("--pinentry-mode", "loopback", "--passphrase-fd", "0"))
    else:
        passphrase = None
    command.append(str(path))
    subprocess.run(command, input=passphrase + "\n" if passphrase else None, text=True, check=True)
    return signature


def checksums(path: Path) -> None:
    content = path.read_bytes()
    for algorithm, extension in (("md5", ".md5"), ("sha1", ".sha1")):
        digest = hashlib.new(algorithm, content).hexdigest()
        Path(f"{path}{extension}").write_text(digest + "\n", encoding="ascii")


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("version")
    parser.add_argument("--unsigned", action="store_true", help="Inspect a local bundle without GPG signatures")
    args = parser.parse_args()
    if not re.fullmatch(r"[0-9]+(?:\.[0-9]+){1,2}", args.version):
        parser.error("version must be a non-snapshot Maven version")

    root = Path(__file__).resolve().parents[2]
    outputs = root / "bazel-bin" / "tools" / "release"
    dist = root / "dist"
    namespace = {"m": "http://maven.apache.org/POM/4.0.0"}
    for artifact in ARTIFACTS:
        pom = ET.parse(outputs / f"{artifact}.pom").getroot()
        actual = tuple(pom.findtext(f"m:{key}", namespaces=namespace) for key in ("groupId", "artifactId", "version"))
        if actual != ("org.realityforge.arez", artifact, args.version):
            raise SystemExit(f"Build outputs do not match requested release: {actual}")
    staging = dist / f"arez-{args.version}"
    bundle = dist / f"arez-{args.version}.zip"
    shutil.rmtree(staging, ignore_errors=True)
    bundle.unlink(missing_ok=True)

    for artifact in ARTIFACTS:
        target = staging / "org" / "realityforge" / "arez" / artifact / args.version
        target.mkdir(parents=True)
        for kind in KINDS:
            source_name = f"{artifact}.{kind}" if kind == "pom" else f"{artifact}{'' if kind == 'jar' else '-' + kind}.jar"
            source = outputs / source_name
            if not source.is_file():
                raise FileNotFoundError(f"Missing Bazel release output: {source}")
            destination = target / filename(artifact, kind, args.version)
            shutil.copyfile(source, destination)
            if not args.unsigned:
                signature = sign(destination)
                checksums(signature)
            checksums(destination)

    with zipfile.ZipFile(bundle, "w", compression=zipfile.ZIP_DEFLATED) as archive:
        for path in sorted(staging.rglob("*")):
            if path.is_file():
                entry = zipfile.ZipInfo(path.relative_to(staging).as_posix(), (1980, 1, 1, 0, 0, 0))
                entry.compress_type = zipfile.ZIP_DEFLATED
                archive.writestr(entry, path.read_bytes())
    print(bundle)


if __name__ == "__main__":
    main()
