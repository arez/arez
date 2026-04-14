# Implementation Plan

## Phase Sequence

1. Capture requirements and resolve the target-component compatibility decision.
2. Update the shared `@CascadeDispose` type compatibility check.
3. Add regression fixtures/tests for parameterized component targets.
4. Update the changelog and run verification gates.

## Delivery Approach

- Patch the shared compatibility logic rather than special-casing methods.
- Reuse the existing processor fixture conventions for compile-success coverage.
- Keep behavior changes limited to `@CascadeDispose` compatibility checks.

## High-Risk Areas

- Risk 1: broadening compatibility may unintentionally permit non-component generic types.
  - Mitigation: gate acceptance on either `arez.Disposable` assignability or `@ArezComponent` presence only.
- Risk 2: field and method paths could diverge again if patched separately.
  - Mitigation: centralize the target-type decision in shared helper logic.

## Required Full Gate

- `bundle exec buildr test`

## Decision Log

| question | concrete plan change |
| --- | --- |
| Q-01 | accept any `@ArezComponent` target in shared `@CascadeDispose` validation, regardless of `disposeNotifier`, and cover the reported parameterized method case with a successful compile fixture |

## Completion Criteria

- The reported parameterized `@ArezComponent` `@CascadeDispose` case compiles.
- Existing bad-type `@CascadeDispose` cases still fail.
- Processor tests cover the regression.
- `CHANGELOG.md` documents the fix.
