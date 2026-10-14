# T05 — Run the complete Persist JVM slice

- Status: complete
- Blocked by: T04
- Spec coverage: R01, R02, R06, AC01, AC02, AC06

## Delivers

Persist core, processor and integration tests run in Bazel; the complete retained JVM baseline is demonstrated.

## Acceptance criteria

- [x] Expose //persist/core, //persist/processor and their :tests/:all_tests plus //persist/integration-tests:tests.
- [x] Preserve the core test's Arez/Grim processing, Mockito dependencies, development options and archive-based architecture analysis.
- [x] Preserve Persist QA's additional Arez processor and its filtering of Arez-generated outputs while checking Persist goldens.
- [x] Create //:jvm_tests covering all eight previously enabled test modules; record the complete baseline inventory.

## Validation

- bazel build //persist/core //persist/processor — builds both Persist products.
- bazel test //persist/core:tests //persist/processor:all_tests //persist/integration-tests:tests --test_output=errors — verifies the complete Persist slice.
- bazel test //:jvm_tests --test_output=errors and build all retained JVM product/doc targets — mandatory JVM parity milestone.

## Evidence

- Both Persist products build with declared Arez/Grim plugins and native JS resources.
- Persist core: 29 tests; integration: 1; processor goldens: 23 (both modes); diagnostics: 31; forked javac: 1. All passed without skips.
- Full `bazel test //:jvm_tests //third_party/java:verify_config_sha256 --test_output=errors` passed all 12 JVM targets plus dependency verification. The retained JVM inventory executes 2,271 TestNG cases across all eight modules.
- All nine product/documentation targets build. Buildifier check passed.
- [Inventory](../persist-test-inventory.json) includes all tracked Persist product/test/fixture sources, concrete test classes, provider rows and aggregate executed counts. Runtime/integration/fixture hashes match the original baseline; only test-support compiler options add Java 17 (the same JDK21 warning fix verified in T02).
- JDepend reads the declared core jar directly; Mockito dependencies and all three development settings are retained. Persist QA retains the additional Arez processor and exact-output filtering.
