# T13 — Restore extras, integrations and compiled documentation

- Status: complete
- Blocked by: T12
- Spec coverage: R01, R02, R04, R06, AC01, AC02, AC04, AC06

## Delivers

Remaining owned JVM consumers and generated examples use JSpecify, restoring the entire retained JVM gate.

## Acceptance criteria

- [x] Migrate extras libraries/tests, main integration models/utilities, documentation examples and their generated consumers.
- [x] Use the migrated Sting processor for optional integration-generated sources. Preserve all integration runtime/event fixture behavior.
- [x] Check generated constructors/nullness against each model's intended behavior, including previously unannotated nullable relationships/setters.
- [x] Complete the T08-T13 migration inventory; remove temporary legacy product nullability dependencies/compatibility code and resolve every tracked migration failure.
- [x] Keep only deliberate legacy-ignorance fixtures or third-party dependencies as justified test/tool inputs.

## Validation

- bazel build //core //processor //extras/dom //extras/promise //extras/spytools //extras/testng //persist/core //persist/processor //doc-examples — builds the complete retained JVM graph.
- bazel test //:jvm_tests --test_output=errors — mandatory fully restored JSpecify JVM milestone.
- Audit migrated maintained/golden sources and final product compile dependencies for old nullability interpretation; preserve unrelated JDK javax namespaces.

## Evidence

- Migrated all 260 original remaining JVM source files, plus marked package descriptors, using canonical JSpecify annotations. [Source membership](../T13-source-membership.json) records ownership. Permissive model fields/setters remain nullable; literal/new-object factories retain non-null returns. Generic callback/value bounds permit null where the core contracts do.
- Extras packages use marked defaults. Promise value/error storage and access are nullable; callback rejection and spy parameter elements allow null. Nullable geolocation coordinates and native storage results are explicit. Qualified nested references use type-use placement or marked defaults rather than old declaration annotations.
- `bazel build //core //processor //extras/dom //extras/promise //extras/spytools //extras/testng //persist/core //persist/processor //doc-examples` passed with the pinned remote Proton/Sting modules. Integration and documentation annotation processing uses the migrated Sting plugin.
- `bazel test //:jvm_tests --test_output=errors` passed all 31 targets. [Test results](../T13-jvm-results.json) record 2,299 TestNG invocations, zero failures and zero skips. This restores the mandatory full JVM JSpecify milestone; all T08–T13 pending slices are resolved.
- Two existing integration assertions exposed annotation text leaking into runtime reference entity names. Generation now renders Java type identity in those messages; the unchanged runtime assertions and all spy-event JSON fixtures pass. Updated only affected golden families, retaining exact output checks and both modes.
- [Case inventory](../T13-case-inventory.json) checks unchanged owned integration/extras method membership and runtime fixture content. Reviewed constructor defaults, nullable setters, required fields and generated reference contracts against their prior behavior.
- [Audit](../T13-nullability-audit.json) finds no old javax nullability in retained owned JVM sources or direct product compile dependencies. Only deliberate ignored-legacy fixtures/tool inputs remain. CssRules retains unrelated org.intellij Language metadata. Two browser bootstrap legacy imports remain explicitly owned by T20–T22; they are not retained JVM product inputs.
- Full product build, full JVM suite, Palantir formatting, Buildifier, whitespace, plan and documentation-link checks pass. Compiler-policy opt-outs remain only for the planned T14–T15 enforcement transition.
