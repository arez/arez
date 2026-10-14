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
  deploy)
    if [[ $# -ne 2 || ! "$2" =~ ^[0-9]+(\.[0-9]+){1,2}$ ]]; then
      echo 'Usage: tools/site.sh deploy <version>' >&2
      exit 2
    fi
    VERSION="$2"
    if [[ "$(git -C "${ROOT}" describe --tags --exact-match HEAD 2>/dev/null || true)" != "v${VERSION}" ]]; then
      echo "Deploy from the v${VERSION} tag." >&2
      exit 1
    fi
    build_site
    bazel run //tools/site:tool -- check "${SITE_DIR}"
    DEPLOY_DIR="$(mktemp -d)"
    trap 'rm -rf "${DEPLOY_DIR}"' EXIT
    git clone --depth 1 https://github.com/arez/arez.github.io.git "${DEPLOY_DIR}"
    python3 - "${SITE_DIR}" "${DEPLOY_DIR}" <<'PY'
from pathlib import Path
import shutil
import sys

source, destination = map(Path, sys.argv[1:])
for path in destination.iterdir():
    if path.name != '.git':
        shutil.rmtree(path) if path.is_dir() else path.unlink()
shutil.copytree(source, destination, dirs_exist_ok=True)
PY
    git -C "${DEPLOY_DIR}" add -A
    if ! git -C "${DEPLOY_DIR}" diff --cached --quiet; then
      git -C "${DEPLOY_DIR}" commit -m "Publish Arez ${VERSION} website"
      git -C "${DEPLOY_DIR}" push origin HEAD:master
    fi
    ;;
  *)
    echo 'Usage: tools/site.sh <build|check|serve|deploy> [version]' >&2
    exit 2
    ;;
esac
