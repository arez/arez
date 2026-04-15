# AutoObserve Requirements

## Mission

- Add `arez.annotations.AutoObserve` so an alive owner component can keep referenced `ComponentObservable` targets observed for the owner’s full alive lifetime, primarily to retain `disposeOnDeactivate = true` components.

## Scope Boundaries

- In scope: annotation API, processor parsing/validation, code generation, warning integration, docs, doc examples, processor fixtures, integration tests, API compatibility baseline, changelog.
- Out of scope: new runtime primitives, activation-window semantics, mixing `@AutoObserve` with `@CascadeDispose`/`@ComponentDependency`, any rename away from `AutoObserve`.

## Locked Decisions

- Name: `@AutoObserve`.
- Lifetime semantics: owner alive lifetime, not activate/deactivate window.
- Supported targets: final fields, concrete zero-arg methods, abstract `@Reference` methods, abstract `@Observable` getters.
- Nullable targets are allowed and skipped via generated null guards.
- `validateTypeAtRuntime = true` is supported only when the declared type is annotated with `@ArezComponentLike`.
- Without runtime validation, declared types may be any type assignable to `arez.component.ComponentObservable`.
- `@AutoObserve` is mutually exclusive with `@CascadeDispose` and `@ComponentDependency`.
- `@AutoObserve` fields must be final.
- `@Reference` targets force resolution, including lazy references.
- Auto-observation must become active during construction by participating in the scheduled `componentComplete()` path.

## Command Surface

- New annotation in `core/src/main/java/arez/annotations/AutoObserve.java` with `@Target({ ElementType.METHOD, ElementType.FIELD })`.
- New parameter: `boolean validateTypeAtRuntime() default false;`
- If any `@AutoObserve` targets exist, generate:
  - a private observer field such as `$$arez$$_$autoObserve`
  - a private method such as `$$arezi$$_$autoObserve()`
- Generated method must:
  - observe field values directly
  - call plain methods and observe returned values
  - call `@Observable` getters so dependency tracking follows the property
  - call `@Reference` accessors or equivalent link path so the reference is resolved before observation
  - emit null guards for nullable targets
- Generated observer must be created in the constructor with:
  - `Observer.Flags.RUN_LATER`
  - `Observer.Flags.NESTED_ACTIONS_DISALLOWED`
  - `Observer.Flags.AREZ_DEPENDENCIES`
- Generated observer must be disposed during generated internal dispose.

## Quality Gates

- Targeted iteration checks:
  - `bundle exec buildr processor:test`
  - targeted integration-test runs for new or affected classes
- Required full gate:
  - `bundle exec buildr ci J2CL=no`
- Submission expectation:
  - `bundle exec buildr test`
- User-visible behavior requires `CHANGELOG.md` update.
- Docs must be updated both in annotation Javadocs and `docs/` pages.

## Open Questions Register

| id | status | question | context | options | tradeoffs | recommended_default | user_decision | artifacts_updated |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Q-01 | resolved | Should lifetime be alive-lifetime or activated-only? | The feature can either own an observer for the whole component lifetime or tie into activation hooks. | Alive lifetime; activated-only. | Alive lifetime matches the intended generated code and keeps semantics simple. | Alive lifetime | Alive lifetime | `00-requirements.md`, `01-feature-auto-observe.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-02 | resolved | How should nullable targets behave? | Generated code either needs null guards or a nonnull-only contract. | Allow null and skip; require nonnull. | Null guards support the examples and avoid surprising runtime failures. | Allow null and skip | Allow null and skip | same |
| Q-03 | resolved | Which targets are supported? | The feature can stay field-only or support method-based accessors. | fields only; fields + plain methods + `@Reference` + `@Observable`. | Supporting method targets fits existing Arez patterns and supports dynamic retargeting. | fields + plain methods + `@Reference` + `@Observable` | fields + plain methods + `@Reference` + `@Observable` | same |
| Q-04 | resolved | How should runtime validation work? | Runtime validation is needed for `@ArezComponentLike` declared types that are only known to implement `ComponentObservable` at runtime. | disallow; allow for all types; allow only for `@ArezComponentLike`. | Allowing it only for `@ArezComponentLike` keeps the escape hatch narrow and intentional. | allow only for `@ArezComponentLike` | accepted | same |
| Q-05 | resolved | Can it coexist with dependency/dispose annotations? | A single reference could otherwise try to participate in multiple lifecycle policies. | allow coexistence; make mutually exclusive. | Mutual exclusion keeps lifecycle policy unambiguous. | mutually exclusive | mutually exclusive | same |
| Q-06 | resolved | Can annotated fields be mutable? | Mutable fields would retain stale observations unless additional rerun triggers were added. | mutable; final only. | Final fields keep behavior sound and push dynamic cases to method targets. | final only | fields must be final | same |
| Q-07 | resolved | Should `@Reference` auto-observe force resolution? | Lazy references are ineffective if auto-observe does not load them. | no; yes. | Forced resolution matches the user intent and avoids a surprising no-op. | yes | yes | same |
| Q-08 | resolved | When should auto-observation first activate? | The internal observer can start only after a later flush or as part of construction scheduling. | later; during construction. | Construction-time scheduling avoids an initial liveness gap. | during construction | during construction | same |
| Q-09 | resolved | What public name should the annotation use? | This is part of the long-term public API surface. | `AutoObserve`; `KeepObserved`; `ObserveReference`. | `AutoObserve` matches existing phrasing and the drafted code. | `AutoObserve` | `AutoObserve` | same |
| Q-10 | resolved | Where should planning artifacts live? | The repo had no existing planning convention. | `plans/auto-observe`; `changes/auto-observe`; `docs/plans/auto-observe`. | `plans/auto-observe` keeps workflow artifacts isolated from user docs. | `plans/auto-observe` | accepted | same |
