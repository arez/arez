#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 2 ]]; then
  echo "Usage: tools/update_goldens.sh <arez|persist> <family>" >&2
  exit 1
fi
case "$1:$2" in
  arez:component|arez:action|arez:observable|arez:memoize|arez:mapped_relationship|arez:observe|arez:lifecycle|arez:relationships|arez:metadata|arez:interop|arez:scenarios|arez:jspecify|arez:basic_component|arez:basic_action|arez:basic_observe|arez:basic_lifecycle|arez:basic_metadata|arez:basic_scenarios)
    module=processor
    package=arez/processor
    prefix=arez
    ;;
  persist:model|persist:types)
    module=persist/processor
    package=arez/persist/processor
    prefix=arez.persist
    ;;
  *)
    echo "Unknown golden family: $1:$2" >&2
    exit 1
    ;;
esac
root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$root"
target="$module/src/test/java/$package:${2}_goldens"
bazel build "//$target"
binary="$(bazel info bazel-bin)/$module/src/test/java/$package/${2}_goldens"
fixture="$module/src/test/fixtures"
scratch="$(mktemp -d "${TMPDIR:-/tmp}/arez-goldens.XXXXXX")"
trap 'python3 -c "import shutil,sys; shutil.rmtree(sys.argv[1])" "$scratch"' EXIT
python3 - "$binary.runfiles/_main/$fixture" "$scratch/fixtures" <<'PY'
import shutil
import sys
shutil.copytree(sys.argv[1], sys.argv[2])
PY
bazel run "//$target" -- "--wrapper_script_flag=--jvm_flag=-D$prefix.fixture_dir=$scratch/fixtures" \
  "--wrapper_script_flag=--jvm_flag=-D$prefix.output_fixture_data=true" -d "$scratch/test-output"
python3 - "$scratch/fixtures" "$root/$fixture" <<'PY'
import shutil
import sys
from pathlib import Path
source, destination = map(Path, sys.argv[1:])
for tree in ("expected", "expectedFormatted"):
    files = sorted((source / tree).rglob("*"))
    for file in files:
        if file.is_file():
            target = destination / tree / file.relative_to(source / tree)
            if not target.is_file():
                raise SystemExit(f"Declare and review the new golden before copying: {target}")
            if file.read_bytes() != target.read_bytes():
                shutil.copyfile(file, target)
PY
bazel test "//$target" --test_output=errors
git diff -- "$fixture/expected" "$fixture/expectedFormatted"
