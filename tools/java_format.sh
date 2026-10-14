#!/usr/bin/env bash
set -euo pipefail

root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
mode="${1:-write}"
case "$mode" in
  write|check|watch) ;;
  *) echo "Usage: tools/java_format.sh [write|check|watch]" >&2; exit 2 ;;
esac
cd "$root"
paths=(
  core/src/main/java
  core/src/test/java
  processor/src/main/java
  processor/src/test/java
  integration-tests/src/test/java
  doc-examples/src/main/java
  extras/dom/src/main/java
  extras/dom/src/test/java
  extras/dom-example/src/main/java
  extras/promise/src/main/java
  extras/promise-example/src/main/java
  extras/spytools/src/main/java
  extras/spytools/src/test/java
  extras/testng/src/main/java
  persist/core/src/main/java
  persist/core/src/test/java
  persist/processor/src/main/java
  persist/processor/src/test/java
  persist/integration-tests/src/test/java
)
format_roots=()
for path in "${paths[@]}"; do
  format_roots+=("--root=$path")
done
case "$mode" in
  check) exec bazel build //:java_format_check ;;
  write) exec bazel run @rules_palantir_java_format//:java_format -- "${format_roots[@]}" ;;
  watch) exec bazel run @rules_palantir_java_format//:java_format_watch -- "${format_roots[@]}" ;;
esac
