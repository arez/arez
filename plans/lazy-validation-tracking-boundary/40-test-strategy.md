# Lazy Validation Tracking Boundary Test Strategy

Status: completed

## Test Goals

- Prove the runtime dependency graph preserves source-level immediate edges after lazy validation.
- Prove current value semantics remain unchanged.
- Prove lifecycle and memoize paths do not regress.
- Assert graph directly; do not assert `WhyRun` strings in this phase.

## Core Graph Matrix

### G-01 No-Change Chain

- Shape: `A -> B -> C`.
- Trigger: mark `C` changed so `B` and `A` become `POSSIBLY_STALE`; recompute/validate so `B` remains equal.
- Expected graph: `A` depends on `B` only; `B` depends on `C`; `C` does not list `A` as an observer.

### G-02 Deep Change Propagates

- Shape: `A -> B -> C`.
- Trigger: `C` changes; `B` recomputes to a different value.
- Expected behavior: `A` becomes `STALE` or recomputes through existing propagation.
- Expected graph: no direct `A -> C` edge after validation/recompute.

### G-03 Equality Suppression

- Shape: `A -> B -> C`.
- Trigger: `C` changes; `B` recomputes but comparator reports equal.
- Expected behavior: `A` does not recompute.
- Expected graph: no direct `A -> C` edge.

### G-04 Dependency Order And Short-Circuit

- Shape: `A -> B1, B2`; `B1 -> C1`; `B2 -> C2`.
- Trigger: `B1` validates as changed.
- Expected behavior: validation stops after first changed direct dependency.
- Expected graph: no leaked direct `A -> C1` or `A -> C2` edge.

### G-05 Dynamic Dependency Swap

- Shape before: `A -> B -> C1`.
- Trigger: `B` recomputes and now reads `C2` instead of `C1`.
- Expected graph: `B -> C2`; `B` no longer observes `C1`; `A` observes only `B`.

### G-06 Repeated/Nested Validation

- Shape: `A -> B -> C -> D`.
- Trigger: deep no-change validation through multiple `POSSIBLY_STALE` levels.
- Expected graph: each computed value observes only its immediate source-level dependency.

### G-07 Suppressed-Then-Direct Read

- Shape: `A -> B`, `B -> C`, and after reading `B`, `A` also reads `C` directly.
- Trigger: `B` is `POSSIBLY_STALE`, so validating `B` recursively touches `C` before `A` reaches its own direct `C` read.
- Expected behavior: recursive validation does not leak an accidental edge, but the later real direct `A -> C` read is recorded normally.
- Expected graph: `A` depends on both `B` and `C` because source code reads both; `B` depends on `C`; observer backlinks match those direct source-level reads.

## Semantics Matrix

### S-01 Cached Error Re-throw

- Trigger: dependency has cached error and rethrows same error during validation.
- Expected behavior: preserve existing `Observer.shouldCompute()` result and state behavior.
- Expected graph: no leaked transitive edge.

### S-02 New Error

- Trigger: dependency validation computes and throws a new error.
- Expected behavior: downstream state transitions match existing tests.
- Expected graph: no leaked transitive edge.

### S-03 `READ_OUTSIDE_TRANSACTION`

- Trigger: recursive validation reaches a computable configured with `READ_OUTSIDE_TRANSACTION`.
- Expected behavior: no recursive validation edge is recorded on the outer tracker.

### S-04 Priority And Lower-Priority Observation

- Trigger: computed and observer priorities matching current priority-sensitive tests.
- Expected behavior: existing priority invariants and scheduling behavior remain valid.
- Expected graph: no flattening.

## Lifecycle Matrix

### L-01 Deactivation

- Trigger: outer `A` no longer observes `B` after a branch change.
- Expected behavior: upstream `C` is not kept alive by leaked `A -> C` observer edge.

### L-02 Hooks

- Trigger: dependency graph changes and deactivation hooks should run.
- Expected behavior: hooks follow immediate graph removal, not leaked transitive observers.

### L-03 Memoize Cache Disposal

- Trigger: memoized computed value loses its observers.
- Expected behavior: memoized disposal behavior is not blocked by leaked direct transitive observers.

## Memoize Matrix

### M-01 No-Arg `@Memoize`

- Generated accessor delegates to backing `ComputableValue.get()`.
- Expected graph: no recursive validation flattening.

### M-02 Parameterized `@Memoize` / `MemoizeCache`

- `MemoizeCache.get()` delegates to `getComputableValue(args).get()`.
- Expected graph: no recursive validation flattening for per-argument computable values.

### M-03 Non-Arez Dependencies

- `AREZ_OR_EXTERNAL_DEPENDENCIES` and `reportPossiblyChanged()` should keep current no-change and changed behavior.
- Expected graph: no recursive validation flattening.

## Suggested Test Locations

- Core graph tests: `core/src/test/java/arez/ObserverTest.java` and `core/src/test/java/arez/ComputableValueTest.java`.
- Transaction/observable invariants: `core/src/test/java/arez/TransactionTest.java` and `core/src/test/java/arez/ObservableValueTest.java` where needed.
- Memoize integration: `integration-tests/src/test/java/arez/integration/memoize/` or a focused nested-computable integration test.

## Existing Graph Assertion Audit

Before finalizing implementation, audit current tests and fixtures that already inspect runtime graph state. At minimum, search for:

```bash
rg "getDependencies|getObservers|asComputableValueInfo|asObserverInfo" core/src/test integration-tests/src/test
```

Known audit starting points from repository inspection:

- `core/src/test/java/arez/ComputableValueInfoImplTest.java`
- `core/src/test/java/arez/ObserverInfoImplTest.java`
- `core/src/test/java/arez/TransactionTest.java`
- `core/src/test/java/arez/ObserverTest.java`
- `core/src/test/java/arez/ComputableValueTest.java`
- `core/src/test/java/arez/ObservableValueTest.java`
- `core/src/test/java/arez/ArezContextTest.java`
- `integration-tests/src/test/java/arez/integration/component_observable/ComponentObservableTest.java`
- `integration-tests/src/test/java/arez/integration/observe/ObserveArezOrNoDependenciesTest.java`
- `integration-tests/src/test/java/arez/integration/**/*.java`

Expected outcome: update only assertions whose old expectations depended on leaked transitive validation edges; leave unrelated spy info behavior unchanged.

## Validation Sequence

1. Run focused core tests for changed classes.
2. Run memoize/nested-computable integration tests.
3. Run `bundle exec buildr arez:core:test`.
4. Run `bundle exec buildr arez:integration-tests:test`.
5. Run full gate `bundle exec buildr ci J2CL=no`.

Buildr commands require Bundler dependencies to be installed first; current workspace evidence shows `braid-1.1.6` is missing.
