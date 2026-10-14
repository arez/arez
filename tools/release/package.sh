#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 1 ]]; then
  echo "Usage: tools/release/package.sh <version> [--unsigned]" >&2
  exit 1
fi

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
VERSION="$1"
shift
if [[ ! "${VERSION}" =~ ^[0-9]+(\.[0-9]+){1,2}$ || $# -gt 1 || ( $# -eq 1 && "$1" != --unsigned ) ]]; then
  echo 'Usage: tools/release/package.sh <numeric-version> [--unsigned]' >&2
  exit 2
fi

cd "${ROOT}"
bazel build //tools/release:maven_artifacts --//tools/release:version="${VERSION}"
python3 tools/release/package.py "${VERSION}" "$@"
