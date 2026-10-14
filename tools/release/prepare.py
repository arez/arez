#!/usr/bin/env python3
"""Move Unreleased notes into a release and update both documented Maven versions."""

import argparse
import re
from datetime import date
from pathlib import Path


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("version")
    args = parser.parse_args()
    version = args.version
    if not re.fullmatch(r"[0-9]+(?:\.[0-9]+){1,2}", version):
        parser.error("expected a numeric release version such as 0.257")

    root = Path(__file__).resolve().parents[2]
    changelog = root / "CHANGELOG.md"
    content = changelog.read_text()
    match = re.search(r"^### \[v([^]]+)\]", content, re.MULTILINE)
    if not match or "### Unreleased\n\n" not in content:
        raise SystemExit("Cannot locate Unreleased notes and previous release")
    previous = match.group(1)
    if f"### [v{version}]" in content:
        raise SystemExit(f"Version {version} is already in the changelog")
    notes = content.split("### Unreleased\n\n", 1)[1].split("### [v", 1)[0].strip()
    if not notes:
        raise SystemExit("No Unreleased notes to publish")
    header = (
        f"### [v{version}](https://github.com/arez/arez/tree/v{version}) "
        f"({date.today().isoformat()}) · "
        f"[Full Changelog](https://github.com/arez/arez/compare/v{previous}...v{version})"
    )
    updates = {changelog: content.replace("### Unreleased\n\n", f"### Unreleased\n\n{header}\n\nChanges in this release:\n\n", 1)}
    for path in (root / "docs/project_setup.md", root / "persist/README.md"):
        updated, count = re.subn(r"<arez.version>[^<]+</arez.version>", f"<arez.version>{version}</arez.version>", path.read_text())
        if count != 1:
            raise SystemExit(f"Expected exactly one arez.version property in {path.relative_to(root)}")
        updates[path] = updated
    for path, updated in updates.items():
        path.write_text(updated)
    print(f"Prepared v{version} from v{previous}; review CHANGELOG.md, docs/project_setup.md and persist/README.md")


if __name__ == "__main__":
    main()
