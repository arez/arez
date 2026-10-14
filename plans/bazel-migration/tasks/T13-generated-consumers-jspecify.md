# T13 — Restore extras, integrations and compiled documentation

- Status: pending
- Blocked by: T12
- Spec coverage: R01, R02, R04, R06, AC01, AC02, AC04, AC06

## Delivers

Remaining owned JVM consumers and generated examples use JSpecify, restoring the entire retained JVM gate.

## Acceptance criteria

- [ ] Migrate extras libraries/tests, main integration models/utilities, documentation examples and their generated consumers.
- [ ] Use the migrated Sting processor for optional integration-generated sources. Preserve all integration runtime/event fixture behavior.
- [ ] Check generated constructors/nullness against each model's intended behavior, including previously unannotated nullable relationships/setters.
- [ ] Complete the T08-T13 migration inventory; remove temporary legacy product nullability dependencies/compatibility code and resolve every tracked migration failure.
- [ ] Keep only deliberate legacy-ignorance fixtures or third-party dependencies as justified test/tool inputs.

## Validation

- bazel build //core //processor //extras/dom //extras/promise //extras/spytools //extras/testng //persist/core //persist/processor //doc-examples — builds the complete retained JVM graph.
- bazel test //:jvm_tests --test_output=errors — mandatory fully restored JSpecify JVM milestone.
- Audit migrated maintained/golden sources and final product compile dependencies for old nullability interpretation; preserve unrelated JDK javax namespaces.

## Evidence

- pending
