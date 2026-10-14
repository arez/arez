#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "${ROOT}"
test "$(bazel --version)" = 'bazel 9.3.0'
bazel run //tools/buildifier:buildifier_check
tools/java_format.sh check
bazel run //tools/ahab:hermeticity.check
bazel run //tools/ahab:hermeticity.check -- --compilation-mode=opt
bazel build //core //processor //extras/dom //extras/promise //extras/spytools //extras/testng \
  //persist/core //persist/processor //doc-examples \
  //extras/promise-example:java //extras/dom-example:java \
  //:core-j2cl //:dom-j2cl //:promise-j2cl //:spytools-j2cl //:persist-core-j2cl \
  //extras/promise:compile_smoke //extras/promise:compile_smoke_dev \
  //extras/spytools:compile_smoke //extras/spytools:compile_smoke_dev \
  //tools/release:maven_artifacts //tools/release:site_javadoc
mkdir -p reports/verification
python3 tools/release/check_artifacts.py --directory "$(bazel info bazel-bin)/tools/release" \
  --evidence reports/verification/artifacts.json
python3 tools/check_bazel_consumer.py
bazel test //:all_tests
bazel test -c opt //:browser_tests
bazel coverage -c dbg //:jvm_tests --combined_report=lcov \
  --instrumentation_filter='^//(core|processor|extras/(dom|spytools)|persist/(core|processor))[:/]'
coverage_report="$(bazel info -c dbg output_path)/_coverage/_coverage_report.dat"
mkdir -p reports/coverage
python3 - "${coverage_report}" reports/coverage/jvm.lcov <<'PY'
from pathlib import Path
import shutil
import sys

source, destination = map(Path, sys.argv[1:])
destination.unlink(missing_ok=True)
shutil.copyfile(source, destination)
PY
python3 tools/check_coverage.py reports/coverage/jvm.lcov --output reports/coverage/summary.json
tools/site.sh build
tools/site.sh check
tools/release/package.sh 0.257 --unsigned
python3 tools/release/verify_bundle.py dist/arez-0.257.zip 0.257 --unsigned
python3 tools/release/check_commands.py
bazel build //tools/release:maven_artifacts //tools/release:site_javadoc
echo 'All Arez checks passed.'
