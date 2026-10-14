#!/usr/bin/env python3
"""Verify a complete signed or unsigned Maven upload bundle before publication."""

import argparse
import hashlib
import zipfile
import xml.etree.ElementTree as ET
from pathlib import Path

from check_artifacts import ARTIFACTS
from package import KINDS, filename


def verify(path: Path, version: str, unsigned: bool) -> None:
    expected = set()
    with zipfile.ZipFile(path) as archive:
        names = archive.namelist()
        if len(names) != len(set(names)):
            raise SystemExit("Duplicate bundle entries")
        for artifact in ARTIFACTS:
            directory = f"org/realityforge/arez/{artifact}/{version}/"
            for kind in KINDS:
                name = directory + filename(artifact, kind, version)
                expected.add(name)
                entries = [name] if unsigned else [name, name + ".asc"]
                for entry in entries:
                    expected.add(entry)
                    data = archive.read(entry)
                    if entry.endswith(".asc") and not data.startswith(b"-----BEGIN PGP SIGNATURE-----"):
                        raise SystemExit(f"Invalid signature envelope: {entry}")
                    for algorithm in ("md5", "sha1"):
                        checksum = entry + "." + algorithm
                        expected.add(checksum)
                        if archive.read(checksum).decode().strip() != hashlib.new(algorithm, data).hexdigest():
                            raise SystemExit(f"Checksum mismatch: {entry}")
                if kind == "pom":
                    pom = ET.fromstring(archive.read(name))
                    ns = {"m": "http://maven.apache.org/POM/4.0.0"}
                    actual = tuple(pom.findtext(f"m:{key}", namespaces=ns) for key in ("groupId", "artifactId", "version"))
                    if actual != ("org.realityforge.arez", artifact, version):
                        raise SystemExit(f"Unexpected POM identity: {actual}")
        if set(names) != expected:
            raise SystemExit(f"Unexpected bundle entries: {set(names) ^ expected}")
    print(f"Verified {len(ARTIFACTS) * len(KINDS)} artifacts and {len(expected)} bundle entries")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("bundle", type=Path)
    parser.add_argument("version")
    parser.add_argument("--unsigned", action="store_true")
    args = parser.parse_args()
    verify(args.bundle, args.version, args.unsigned)
