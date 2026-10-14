#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 1 ]]; then
  echo "Usage: tools/update_runtime_fixtures.sh core-invariants | integration <class.method>" >&2
  exit 1
fi
root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$root"
scratch="$(mktemp -d "${TMPDIR:-/tmp}/arez-runtime-fixtures.XXXXXX")"
trap 'python3 -c "import shutil,sys; shutil.rmtree(sys.argv[1])" "$scratch"' EXIT
case "$1" in
  core-invariants)
    [[ $# -eq 1 ]]
    target=core/src/test/java/arez:tests
    fixture=core/src/test/java/arez/diagnostic_messages.json
    cp "$fixture" "$scratch/diagnostic_messages.json"
    bazel build "//$target"
    bazel run "//$target" -- "--wrapper_script_flag=--jvm_flag=-Darez.diagnostic_messages_file=$scratch/diagnostic_messages.json" \
      --wrapper_script_flag=--jvm_flag=-Darez.output_fixture_data=true -d "$scratch/test-output"
    cp "$scratch/diagnostic_messages.json" "$fixture"
    bazel test "//$target" --test_output=errors
    git diff -- "$fixture"
    ;;
  integration)
    [[ $# -eq 2 ]]
    selection="$2"
    fixture="$(python3 - "$selection" <<'PY'
import re
import sys
from pathlib import Path
selection = sys.argv[1]
if not re.fullmatch(r"arez\.integration\.[\w.]+\.\w+", selection):
    raise SystemExit("Expected a fully qualified integration test class and method")
classname, method = selection.rsplit(".", 1)
source = Path("integration-tests/src/test/java") / (classname.replace(".", "/") + ".java")
if not source.is_file() or not re.search(r"public void\s+" + re.escape(method) + r"\s*\(", source.read_text()):
    raise SystemExit(f"Unknown integration method: {selection}")
fixture = Path(classname.replace(".", "/") + "." + method + ".json")
if not (Path("integration-tests/src/test/resources") / fixture).is_file():
    raise SystemExit(f"Declare and review a new fixture first: {fixture}")
print(fixture)
PY
)"
    bazel build //integration-tests:tests
    binary="$(bazel info bazel-bin)/integration-tests/tests"
    python3 - "$binary.runfiles/_main/integration-tests/src/test/resources" "$scratch/resources" <<'PY'
import shutil
import sys
shutil.copytree(sys.argv[1], sys.argv[2])
PY
    bazel run //integration-tests:tests -- "--wrapper_script_flag=--jvm_flag=-Darez.integration_fixture_dir=$scratch/resources" \
      --wrapper_script_flag=--jvm_flag=-Darez.output_fixture_data=true -methods "$selection" -d "$scratch/test-output" > "$scratch/test.log" 2>&1
    cat "$scratch/test.log"
    python3 - "$scratch/test.log" <<'PY'
import re
import sys
from pathlib import Path
counts = re.search(r"Total tests run: (\d+), Passes: (\d+), Failures: (\d+), Skips: (\d+)", Path(sys.argv[1]).read_text())
if not counts or tuple(map(int, counts.groups())) != (1, 1, 0, 0):
    raise SystemExit("Expected exactly one passing fixture scenario")
PY
    cp "$scratch/resources/$fixture" "integration-tests/src/test/resources/$fixture"
    bazel test //integration-tests:tests --test_output=errors --test_arg=-methods --test_arg="$selection"
    git diff -- "integration-tests/src/test/resources/$fixture"
    ;;
  *)
    echo "Unknown runtime fixture selection: $1" >&2
    exit 1
    ;;
esac
