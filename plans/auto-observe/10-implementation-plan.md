# Implementation Plan

## Phase Sequence

1. Create and maintain structured-delivery artifacts under `plans/auto-observe/`.
2. Add the public `@AutoObserve` annotation, changelog entry, and API compatibility updates.
3. Extend processor parsing, validation, warning integration, and component scheduling metadata.
4. Generate owner-lifetime auto-observer fields, methods, initialization, and disposal.
5. Add processor fixtures and negative coverage.
6. Add integration tests covering owner-lifetime keepalive behavior.
7. Update docs and examples, then run full gates and close out.

## Delivery Approach

- Execute one task at a time with minimal diffs.
- Validate iteratively with targeted checks.
- Run full gates before marking the final validation task complete.
- Keep plan, task board, docs, and code aligned as behavior lands.

## Task Granularity Rules

- Keep each task independently verifiable.
- Split behavior from coverage only where it reduces risk.
- Include tests and documentation in the same behavioral slice where practical.

## High-Risk Areas

- Risk 1: incorrect initial scheduling causes a construction-time liveness gap.
  - Impact: referenced `disposeOnDeactivate` components may dispose before first auto-observe run.
  - Mitigation: extend `ComponentDescriptor.requiresSchedule()` for auto-observe presence and verify constructor flow in generated fixtures and integration tests.
- Risk 2: `@Observable` and `@Reference` targets may track or resolve incorrectly.
  - Impact: observer fails to retarget, or lazy references remain unresolved and ineffective.
  - Mitigation: implement target-specific generation paths and add dedicated processor fixtures plus integration coverage for retargeting and lazy-reference resolution.
- Risk 3: runtime-validation path becomes broader than intended.
  - Impact: arbitrary interface-typed targets compile and fail unpredictably at runtime.
  - Mitigation: reject `validateTypeAtRuntime = true` unless the declared type is annotated with `@ArezComponentLike`, and add negative fixtures for disallowed variants.

## Required Full Gates

`bundle exec buildr ci J2CL=no`

## Decision Log

| question | concrete plan change |
| --- | --- |
| Q-01 | generate one owner-lifetime observer instead of activate/deactivate hooks |
| Q-02 | emit null guards in generated observe method |
| Q-03 | support fields, plain methods, `@Reference`, and `@Observable` getters |
| Q-04 | add `validateTypeAtRuntime` with `@ArezComponentLike`-only validation rule |
| Q-05 | add processor errors for coexistence with dependency and dispose annotations |
| Q-06 | enforce final fields |
| Q-07 | force reference resolution in generated auto-observe code |
| Q-08 | include auto-observe in construction scheduling path |
| Q-09 | use public API name `AutoObserve` |
| Q-10 | store workflow artifacts under `plans/auto-observe/` |

## Completion Criteria

- All planned tasks completed.
- Evidence recorded for each completed task.
- Final gates passing.
- Working tree clean or documented exception.
