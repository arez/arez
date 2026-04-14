# Feature Requirements Deep Dive

## Feature

- Name: `@AutoObserve`
- Owner: Codex
- Related plan and task IDs: `T01`-`T07`

## Problem Statement

- Current lifecycle-management choices for component references are disposal ownership via `@CascadeDispose` and dispose-listener dependency handling via `@ComponentDependency`.
- There is no declarative way for an owner component to keep a referenced `disposeOnDeactivate` component alive simply by observing it for as long as the owner is alive.

## Scope

- In scope:
  - new annotation API
  - processor parsing and validation
  - generated owner-lifetime observer wiring
  - unmanaged-reference warning integration
  - docs, examples, changelog, API baseline
  - processor and integration coverage
- Out of scope:
  - new runtime primitives
  - activation-window semantics
  - mixed lifecycle policy on a single target

## Inputs and Interfaces

- New annotation: `arez.annotations.AutoObserve`
- New parameter: `validateTypeAtRuntime`
- No runtime API surface changes outside generated code and the new annotation.

## Behavior Requirements

1. `@AutoObserve` may annotate fields and methods.
2. Fields must be final, non-private, non-static, and subclass-callable.
3. Methods must be zero-arg, non-private, non-static, non-throwing, return a value, and be subclass-callable.
4. Abstract methods are valid only when paired with `@Reference` or `@Observable`.
5. `@AutoObserve` on `@Observable` getters must track the getter and property so retargeting reschedules the internal observer.
6. `@AutoObserve` on `@Reference` accessors must force resolution even when the reference is lazy.
7. Nullable targets must be skipped safely with generated null guards.
8. Non-runtime-validated targets must be assignable to `ComponentObservable`.
9. `validateTypeAtRuntime = true` must only be valid for declared types annotated with `@ActAsComponent`.
10. `@AutoObserve` must suppress unmanaged-component-reference warnings on supported targets.
11. `@AutoObserve` must be rejected if combined with `@CascadeDispose` or `@ComponentDependency`.
12. If any auto-observe targets exist, the component must require scheduling during construction and dispose the generated observer during component disposal.

## Error Handling and Diagnostics

- Invalid field mutability, visibility, static-ness, parameters, throws clauses, unsupported type shapes, and illegal annotation combinations must produce processor errors following existing annotation-style wording.
- Invalid `validateTypeAtRuntime = true` on non-`@ActAsComponent` declared types must produce a dedicated processor error.
- Unmanaged-reference warning text must treat `@AutoObserve` as an accepted lifecycle-management annotation.

## Compatibility and Parity

- This is a new public API and requires `api-test` fixture updates.
- No intentional divergence from current Arez lifecycle rules beyond adding the new annotation path.
- Existing warnings for unmanaged references remain unchanged unless `@AutoObserve` is present.

## Non-Functional Requirements

- Determinism:
  - generated names and observer wiring must be stable across builds
- Performance:
  - only generate the internal observer when a component actually has auto-observe targets
- Security and privacy:
  - no impact

## Acceptance Criteria

- [ ] New annotation compiles, documents behavior, and is exported like other annotations.
- [ ] Generated code creates and disposes an internal auto-observer only when needed.
- [ ] Auto-observe starts during construction and remains active for owner alive lifetime.
- [ ] Nullable, `@Observable`, and `@Reference` targets behave as locked above.
- [ ] Runtime-validation path works only for `@ActAsComponent`.
- [ ] Warning suppression and invalid-combination errors are covered.
- [ ] Docs, examples, changelog, and API baseline are aligned with final behavior.
- [ ] Integration tests prove owner-lifetime keepalive behavior.

## Validation Plan

- Targeted checks:
  - `bundle exec buildr processor:test`
  - targeted integration-test runs for affected test classes
- Full gates:
  - `bundle exec buildr ci J2CL=no`
  - `bundle exec buildr test`

## Open Questions

- None.
