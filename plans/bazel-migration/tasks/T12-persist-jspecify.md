# T12 — Restore the Persist slice after the nullability cut

- Status: pending
- Blocked by: T10, T11
- Spec coverage: R02, R04, R06, AC02, AC04, AC06

## Delivers

Persist runtime models, its processor fixtures and its integration-generated code pass with current contracts.

## Acceptance criteria

- [ ] Migrate Persist core/test/integration sources and Persist QA inputs/output expectations to JSpecify by intent.
- [ ] Preserve persistence skip-null/restore-present behavior, converters, web storage facades and public signatures.
- [ ] Check inherited/compiled members without inventing stronger Persist storage invariants; preserve the additional Arez processor and filtered output sets.
- [ ] Regenerate both Persist golden modes and retained diagnostic/warning/forked-javac scenarios.

## Validation

- bazel build //persist/core //persist/processor — verifies both product targets.
- bazel test //persist/core:tests //persist/processor:all_tests //persist/integration-tests:tests --test_output=errors — restores the full Persist slice.
- Run representative null-valued persistence/restore and multi-processor golden cases; compare storage behavior with the baseline.

## Evidence

- pending
