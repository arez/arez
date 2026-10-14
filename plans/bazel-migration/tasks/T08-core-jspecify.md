# T08 — Migrate core API and runtime contracts to JSpecify

- Status: pending
- Blocked by: T07
- Spec coverage: R02, R04, AC02, AC04

## Delivers

Core product/test contracts express their existing nullness through JSpecify, including array and callback positions.

## Acceptance criteria

- [ ] Mark owned scopes and replace legacy nullability by intended meaning; distinguish nullable arrays from nullable elements in action parameters.
- [ ] Preserve parameter validation, invariants, dispose behavior and public Java signatures. Keep unrelated JetBrains/JDK annotations where used.
- [ ] Update core architecture expected external packages for JSpecify without weakening internal dependency assertions.
- [ ] Start the explicitly bounded T08-T13 migration window. Record any not-yet-migrated processor/consumer failures and their task owner; no suite is dropped.
- [ ] Add focused meaningful checks for migrated nullability positions and preserve core runtime tests.

## Validation

- bazel build //core and bazel test //core:tests --test_output=errors — verifies the migrated runtime slice.
- Inspect selected compiled array/callback type annotations and source contracts — checks placement rather than import substitution.
- Run relevant aggregate JVM checks and record migration-window failures with owners instead of claiming full parity.

## Evidence

- pending
