#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 1 || ! "$1" =~ ^[0-9]+(\.[0-9]+){1,2}$ ]]; then
  echo 'Usage: tools/release/github_release.sh <version>' >&2
  exit 2
fi
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
VERSION="$1"
cd "${ROOT}"
if [[ "$(git describe --tags --exact-match HEAD 2>/dev/null || true)" != "v${VERSION}" ]]; then
  echo "Create the GitHub release from the v${VERSION} tag." >&2
  exit 1
fi
NOTES="$(mktemp)"
trap 'rm -f "${NOTES}"' EXIT
python3 - "${VERSION}" "${NOTES}" <<'PY'
from pathlib import Path
import re
import sys

version, output = sys.argv[1:]
pattern = rf'^### \[v{re.escape(version)}\][^\n]*\n(.*?)(?=^### |\Z)'
match = re.search(pattern, Path('CHANGELOG.md').read_text(), re.MULTILINE | re.DOTALL)
if not match:
    raise SystemExit(f'No changelog notes for v{version}')
Path(output).write_text(match.group(1).strip() + '\n')
PY
gh release create "v${VERSION}" --repo arez/arez --verify-tag --title "Arez ${VERSION}" --notes-file "${NOTES}"
