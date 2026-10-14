# T03 — Compile generated integrations and documentation examples

- Status: complete
- Blocked by: T02
- Spec coverage: R01, R02, R06, AC01, AC02, AC06

## Delivers

Bazel annotation processing compiles the main integration suite and documentation examples, and checks spy-event fixtures.

## Acceptance criteria

- [x] Expose //integration-tests:tests and //doc-examples using explicit internal Arez/Sting plugins and formatter exports.
- [x] Declare event JSON and other integration resources as runfiles; preserve development configuration and assertions.
- [x] Retain every integration class and compilation path, including generated helpers and optional Sting integration.
- [x] Do not perform the GWT bootstrap/support cut yet; any temporary GWT dependency needed for baseline examples is owned by T26 for removal.

## Validation

- bazel build //doc-examples — compiles source and generated examples used by the site.
- bazel test //integration-tests:tests --test_output=errors — checks runtime behavior and spy-event expectations.
- Compare the test-class/resource inventory against the original module — demonstrates complete retained coverage.

## Evidence

- `bazel build //doc-examples` passed, compiling all 76 sources with Arez/Sting processors.
- `bazel test //integration-tests:tests --test_output=errors` passed: 191 tests, zero failures/skips across 141 concrete test classes.
- [Inventory](../integration-doc-inventory.json) records every source/class and all 36 event JSON files; SHA-256 comparisons against baseline 79dcedb949b0033f4325eefa7b5b9aa87431c2d8 passed unchanged.
- `bazel test //third_party/java:verify_config_sha256` and `bazel run //tools/buildifier:buildifier_check` passed.
- JavaBuilder supplies the formatter exports; generated code needs an explicit braincheck dependency under strict-deps. No GWT dependency is used by documentation source imports.
