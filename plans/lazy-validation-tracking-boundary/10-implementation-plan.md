# Lazy Validation Tracking Boundary Implementation Plan

Status: completed

## Delivery Approach

This accepted plan is now in implementation.

The implementation should hard-cut the accidental flattened dependency graph by introducing a narrow recursive-validation observation boundary. The normal `ComputableValue.get()` path remains source-level tracking behavior; only internal validation reads triggered while checking `POSSIBLY_STALE` dependencies should avoid appending observations to the outer tracker.

## Ordered Phase Sequence

### Phase 1: Characterization Tests

- Add focused core tests that reproduce graph flattening without changing production code first.
- Assert direct dependency and observer backlinks, not `WhyRun` output.
- Cover no-change validation, equality-suppressed deep change, deep change that propagates, multi-dependency validation order, dynamic dependency swap, exceptions, deactivation, and hooks.
- Include a suppressed-then-direct-read case where recursive validation touches an observable before the outer computation later reads that same observable directly; the later direct read must still be recorded.

### Phase 2: Internal Suppression Primitive

- Add an internal stack-scoped observation-suppression mechanism.
- Keep it out of public API.
- Ensure `try/finally` restoration and nested recursion safety.
- Keep normal unsuppressed observation path cheap.
- Suppression must skip all observation bookkeeping for the active tracking transaction, including observed-list append, `lastTrackerTransactionId` updates, and equivalent duplicate-elimination state.
- If implemented in `Transaction.observe()`, branch before reading or setting the observable's last-tracker transaction marker.

### Phase 3: Validation-Aware Computable Path

- Replace `Observer.shouldCompute()` validation calls to public `ComputableValue.get()` with an internal validation-aware path.
- Preserve immediate dependency observation before recursive suppression begins.
- Suppress recursive validation observations only.
- Allow actual recompute transactions to track normally.

### Phase 4: Propagation And Lifecycle Verification

- Verify `reportPossiblyChanged()` and `reportChangeConfirmed()` semantics remain unchanged.
- Verify equality comparator no-change behavior remains the downstream gate.
- Verify cached-error and new-error paths preserve current behavior.
- Verify deactivation and hook behavior use immediate graph edges.

### Phase 5: Memoize And Integration Coverage

- Add memoize-level tests for no-arg and parameterized `MemoizeCache`/generated `@Memoize` paths.
- Audit existing graph-facing spy assertions and fixtures, including tests that call `getDependencies()`, `getObservers()`, `asComputableValueInfo()`, or `asObserverInfo()`.
- Review existing nested-computable integration fixtures and scheduler event tests for intentional changes caused by graph cleanup.

### Phase 6: Documentation And Release Notes

- Update `CHANGELOG.md` under `Unreleased` because runtime dependency graph, scheduling, diagnostics, and deactivation behavior are user-visible.
- Only update public docs if implementation introduces a user-facing concept. Internal validation semantics can usually remain in code comments and tests.

## High-Risk Areas And Mitigations

- Risk: suppression hides real source-level dependency reads.
  Mitigation: apply suppression only to recursive validation reads and preserve immediate dependency observation.
- Risk: suppression partially skips tracking but still mutates duplicate-detection markers, causing a later real direct read in the same transaction to be dropped.
  Mitigation: make suppressed observations return before any last-tracker marker or equivalent transaction-local dedupe state is touched; add a suppressed-then-direct-read graph test.
- Risk: recomputing computables fail to refresh dynamic dependencies.
  Mitigation: suspend suppression or use normal tracking when actual recompute transactions run.
- Risk: read/write invariants break inside computable transactions.
  Mitigation: avoid normal nested read-only actions; use a narrow internal observation suppression primitive.
- Risk: equality no-change semantics regress.
  Mitigation: add explicit equality and custom comparator tests.
- Risk: exception semantics regress.
  Mitigation: extend existing `shouldCompute_POSSIBLY_STALE_*` coverage with graph assertions.
- Risk: lifecycle behavior changes unintentionally.
  Mitigation: add deactivation and hook tests that assert retained observer backlinks before and after validation.
- Risk: performance regression in hot read path.
  Mitigation: use a cheap suppression-depth check and avoid per-read allocations.

## Required Full Gate

- `bundle exec buildr ci J2CL=no`

Setup evidence: `bundle exec buildr --tasks | rg "arez:(core|integration-tests):test|\\bci\\b"` could not run because Bundler reported missing gem `braid-1.1.6`. Run `bundle install` before relying on Buildr validation in this workspace.

## Targeted Validation Commands

Command names are inferred from the `buildfile` project names and should be verified after Bundler setup:

- `bundle exec buildr arez:core:test`
- `bundle exec buildr arez:integration-tests:test`
- `bundle exec buildr test`

## Decision Log

- Q-01: Treat as runtime graph correctness. Plan fixes core tracking, not `WhyRun`.
- Q-02: Isolate recursive validation. Plan adds validation boundary.
- Q-03: Preserve recompute tracking. Plan keeps actual compute transactions normal.
- Q-04: Preserve equality gate. Test strategy includes equality no-change and custom comparator cases.
- Q-05: Preserve exception semantics. Test strategy includes cached-error and new-error cases.
- Q-06: Avoid normal nested non-tracking transactions. Plan uses internal suppression.
- Q-07: Suppress all recursive validation observations. Plan does not limit suppression to computed observables.
- Q-08: Include `READ_OUTSIDE_TRANSACTION`. Test strategy includes this path.
- Q-09: Assert graph directly. Plan excludes `WhyRun` string tests.
- Q-10: Use broad regression matrix. Test strategy records the matrix.
- Q-11: No compatibility shim. Plan hard-cuts accidental behavior.
- Q-12: Accept intentional fixture/event fallout. Plan calls for deliberate fixture review.
- Q-13: Use explicit stack-safe internal API. Plan requires `try/finally` and nesting safety.
- Q-14: Require plan approval before implementation. Task board includes `PLAN-APPROVAL`.
- R1-F1: Suppressed observations must not mutate dedupe bookkeeping. Added direct requirement and regression case.
- R1-F2: Existing graph-facing spy assertion tests need audit coverage. Added explicit implementation task and test-strategy audit.
- R1-F3: Plan approval state needs one authoritative source. `20-task-board.yaml` `plan.status` is authoritative.
- R2-F1: Correct graph-audit paths and broaden the file list to actual repo packages plus graph assertion patterns.
- R2-F2: Review-loop completion requires round count/evidence updates and explicit `PLAN-REVIEW` completion before approval.

## Implementation Notes For Human Design Review

The likely implementation shape is:

- Add a transaction/context-scoped suppression counter or save/restore flag.
- Make `Transaction.observe()` return before appending to `_observableValues` or touching `ObservableValue` last-tracker transaction state when suppression is active.
- Add an internal `ComputableValue` validation path that records the immediate dependency edge, then evaluates `shouldCompute()` with recursive validation suppression enabled.
- Ensure suppression is not active during `Observer.invokeReaction()`/`rawObserve()` recompute transactions.

Plan approval is recorded in `plans/lazy-validation-tracking-boundary/20-task-board.yaml` `plan.status`; markdown `Status:` headers are mirrors for readability.

This is a design sketch, not a required patch shape. The implementer should verify the smallest viable change against tests.
