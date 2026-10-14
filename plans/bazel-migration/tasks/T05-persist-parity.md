# T05 — Run the complete Persist JVM slice

- Status: pending
- Blocked by: T04
- Spec coverage: R01, R02, R06, AC01, AC02, AC06

## Delivers

Persist core, processor and integration tests run in Bazel; the complete retained JVM baseline is demonstrated.

## Acceptance criteria

- [ ] Expose //persist/core, //persist/processor and their :tests/:all_tests plus //persist/integration-tests:tests.
- [ ] Preserve the core test's Arez/Grim processing, Mockito dependencies, development options and archive-based architecture analysis.
- [ ] Preserve Persist QA's additional Arez processor and its filtering of Arez-generated outputs while checking Persist goldens.
- [ ] Create //:jvm_tests covering all eight previously enabled test modules; record the complete baseline inventory.

## Validation

- bazel build //persist/core //persist/processor — builds both Persist products.
- bazel test //persist/core:tests //persist/processor:all_tests //persist/integration-tests:tests --test_output=errors — verifies the complete Persist slice.
- bazel test //:jvm_tests --test_output=errors and build all retained JVM product/doc targets — mandatory JVM parity milestone.

## Evidence

- pending
