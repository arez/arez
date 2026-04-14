# Cascade Dispose Parameterized Target Requirements

## Mission

- Fix the processor so valid `@CascadeDispose` targets are accepted when the declared type is a parameterized `@ArezComponent`.

## Scope Boundaries

- In scope: `@CascadeDispose` processor validation for fields and methods, processor fixtures/tests, changelog, and structured-delivery artifacts.
- Out of scope: generated disposal runtime changes, unrelated annotation validation, or broader generic-type compatibility refactors.

## Locked Decisions

- The fix must preserve existing invalid rejections for non-disposable, non-component types.
- The fix should be applied at the shared validation layer so field and method targets remain consistent.
- Add regression coverage for the reported parameterized component case.

## Command Surface

- Update the processor compatibility check used by `@CascadeDispose`.
- Add processor fixtures/tests for parameterized `@ArezComponent` targets.
- Update `CHANGELOG.md` for the user-visible bug fix.

## Quality Gates

- Targeted iteration checks:
  - `bundle exec buildr arez:processor:test`
- Required full gate:
  - `bundle exec buildr test`

## Open Questions Register

| id | status | question | context | options | tradeoffs | recommended_default | user_decision | artifacts_updated |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Q-01 | resolved | Should `@CascadeDispose` accept any `@ArezComponent` target, including parameterized types whose component annotation resolves `disposeNotifier = DISABLE`? | Generated cascade-dispose code invokes `Disposable.dispose(...)`, and generated Arez component implementations are disposable even when dispose notification is disabled. The current validation still rejects some annotated component targets, including the reported example. | accept all `@ArezComponent` targets; keep rejecting component targets when `disposeNotifier = DISABLE`. | Accepting all annotated components matches generated behavior and the reported example, but slightly broadens compile-time acceptance. Keeping the restriction preserves current validation semantics but leaves the reported case broken and inconsistent with disposal code generation. | accept all `@ArezComponent` targets | accept all `@ArezComponent` targets | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
