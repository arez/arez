# T08 — Migrate core API and runtime contracts to JSpecify

- Status: complete
- Blocked by: T07
- Spec coverage: R02, R04, AC02, AC04

## Delivers

Core product/test contracts express their existing nullness through JSpecify, including array and callback positions.

## Acceptance criteria

- [x] Mark owned scopes and replace legacy nullability by intended meaning; distinguish nullable arrays from nullable elements in action parameters.
- [x] Preserve parameter validation, invariants, dispose behavior and public Java signatures. Keep unrelated JetBrains/JDK annotations where used.
- [x] Update core architecture expected external packages for JSpecify without weakening internal dependency assertions.
- [x] Start the explicitly bounded T08-T13 migration window. Record any not-yet-migrated processor/consumer failures and their task owner; no suite is dropped.
- [x] Add focused meaningful checks for migrated nullability positions and preserve core runtime tests.

## Validation

- bazel build //core and bazel test //core:tests --test_output=errors — verifies the migrated runtime slice.
- Inspect selected compiled array/callback type annotations and source contracts — checks placement rather than import substitution.
- Run relevant aggregate JVM checks and record migration-window failures with owners instead of claiming full parity.

## Evidence

- Core product/test packages are NullMarked; owned javax nullability imports/annotations are removed. Unrelated MagicConstant/Grim/JsInterop annotations remain.
- Nullable action arrays use `@Nullable Object @Nullable []`: array and elements are independent. Spy arrays require the array and permit nullable elements; event maps permit nullable values. Value callbacks/observable/computable/memoize generics have nullable Object bounds with bare parametric T results.
- `bazel build //core` and `bazel test //core:tests --test_output=errors` passed: original 843 cases plus two focused contract checks, no failures/skips. Reflection checks compiled array layers, package defaults and callback bounds; runtime checks preserve null results/arguments.
- The architecture check identified the new external JSpecify package. Added exact expected edges from the five marked packages; all pre-existing internal constraints remain and the test passes.
- Full `bazel test //:jvm_tests --test_output=errors` passed all 22 targets / 2,273 TestNG cases at this checkpoint. No not-yet-migrated suite currently fails. T09 will cut processor interpretation, T10–T13 own remaining fixture/consumer migration; aggregate inventory remains unchanged.
- Independent token audit of every product file proved all runtime statements/signatures unchanged after excluding nullness annotations, implicit Object bounds, imports and comments. Core Javadocs and Unreleased notes describe the accepted contracts.
- Formatter, Buildifier and diff whitespace checks passed. Strict checking is introduced in T14–T15, so this checkpoint claims runtime/metadata parity rather than checker completion.
