#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ACTION="${1:-}"
SITE_DIR="${ROOT}/reports/site"

build_site() {
  cd "${ROOT}"
  bazel build //website:site
  local site_output
  site_output="$(bazel info bazel-bin)/website/site"
  python3 - "${site_output}" "${SITE_DIR}" <<'PY'
from pathlib import Path
import shutil
import sys

source, destination = map(Path, sys.argv[1:])
if destination.exists():
    shutil.rmtree(destination)
shutil.copytree(source, destination)
for path in [destination, *destination.rglob('*')]:
    path.chmod(path.stat().st_mode | 0o200)
PY
}

case "${ACTION}" in
  build) build_site ;;
  check)
    cd "${ROOT}"
    exec bazel run //tools/site:tool -- check "${SITE_DIR}"
    ;;
  serve)
    build_site
    exec bazel run //tools/site:tool -- serve "${SITE_DIR}"
    ;;
  *)
    echo 'Usage: tools/site.sh <build|check|serve>' >&2
    exit 2
    ;;
esac
