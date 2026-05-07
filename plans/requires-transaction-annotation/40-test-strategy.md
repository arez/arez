# RequiresTransaction Test Strategy

## Objectives

- Prove the public API compiles and is reflected in API compatibility metadata.
- Prove the processor rejects invalid `@RequiresTransaction` usage with stable diagnostics.
- Prove generated code enforces transaction preconditions without creating transactions.
- Prove runtime behavior across transaction presence, mode, tracking, and feature-flag edge cases.

## Targeted Iteration Checks

1. `bundle exec buildr arez:processor:test`
   - Use while iterating on annotation parsing, validation, and generated fixtures.
2. `bundle exec buildr arez:core:test`
   - Use while iterating on runtime invariant behavior or any helper changes in `core`.
3. `bundle exec buildr arez:integration-tests:test`
   - Use after generated runtime behavior and integration scenarios are in place.

## Required Full Gate

- `bundle exec buildr test PRODUCT_VERSION=0.244 PREVIOUS_PRODUCT_VERSION=0.243`

## Processor Coverage Matrix

- Positive compile fixtures:
  - component containing only `@RequiresTransaction` methods
  - `@ArezComponent(allowEmpty = true)` containing only `@RequiresTransaction` methods
  - basic `@RequiresTransaction`
  - `mode = READ_ONLY`
  - `mode = READ_WRITE`
  - `tracking = TRACKING`
  - `tracking = NON_TRACKING`
  - combined `mode` + `tracking`
  - inherited-method usage
  - interface default-method usage
- Negative compile fixtures:
  - method outside `@ArezComponent` / `@ArezComponentLike`
  - `private`, `static`, `final`, `abstract`
  - mixed with every conflicting Arez method annotation from the shared processor detector/list
  - inaccessible inherited/package-access edge case if the generated implementation cannot legally call the method
- Warning coverage:
  - `@ArezComponent(allowEmpty = true)` containing only `@RequiresTransaction` methods does not emit the unnecessary-`allowEmpty` warning
- Generated expected output:
  - guard ordering relative to initialization/construction/disposal checks
  - active-transaction assertion
  - mode assertion when configured
  - tracking assertion when configured
  - direct delegation with no action/transaction wrapper

## Runtime / Integration Scenario Matrix

- Success cases:
  - call inside a read-write non-tracking action with default `ANY`/`ANY`
  - call inside a read-only transaction with `mode = READ_ONLY`
  - call inside a tracking observer transaction with `tracking = TRACKING`
- Failure cases:
  - call outside any transaction
  - `mode = READ_WRITE` inside a read-only transaction
  - `tracking = TRACKING` inside a non-tracking transaction
  - `tracking = NON_TRACKING` inside a tracking transaction
- Edge cases:
  - `mode = READ_ONLY` and `mode = READ_WRITE` when transaction-type enforcement is disabled
  - inherited-method invocation path
  - interface default-method invocation path
  - call from a disposed/disposal-sensitive component path to confirm existing lifecycle guards still fire first when applicable
  - spy recorder assertions proving no additional action/transaction events are emitted by `@RequiresTransaction` itself

## Diagnostics Expectations

- Processor diagnostics should identify the annotation name and the violated structural rule.
- Runtime diagnostics should include:
  - method identity where available,
  - expected mode or tracking state,
  - actual mode or tracking state when a mismatch occurs.
  - exact Arez-style invariant or API-invariant code text as recorded in `diagnostic_messages.json`

## Evidence Recording Requirements

- Record the exact command and result for each completed implementation task in `20-task-board.yaml`.
- Record the diagnostic catalog update command/result or exact-message verification result for the runtime diagnostics task.
- If the full gate fails for unrelated reasons, record:
  - the exact command,
  - the failing module/test,
  - why the failure is unrelated to `@RequiresTransaction`.
