# Default Equality Comparator Requirements

## Mission

- Add a type-level annotation that lets downstream projects define the default `EqualityComparator` once per value type and have `@Observable` and `@Memoize` derive that comparator when not explicitly configured.

## Scope Boundaries

- In scope: annotation API, annotation default changes, processor resolution/validation, generated output fixtures, integration tests, API compatibility baseline, docs, doc examples, changelog.
- Out of scope: hierarchy walking, type-use annotations, array/JDK/third-party type defaulting, runtime comparator registries.

## Locked Decisions

- New annotation name: `@DefaultEqualityComparator`.
- Package: `arez.annotations`.
- Target: `ElementType.TYPE`.
- Resolution order: explicit `equalityComparator` on `@Observable`/`@Memoize` -> exact declared type annotation -> `ObjectsEqualsComparator`.
- Exact declared type only. No superclass or interface traversal beyond the declared type itself.
- The sentinel for “derive default” is `EqualityComparator.class` as the default value in `@Observable` and `@Memoize`.
- Any effective comparator used in generated code must be instantiable via an accessible no-arg constructor.
- Arrays, JDK types, and third-party types remain explicit-annotation scenarios in v1.

## Command Surface

- Add `arez.annotations.DefaultEqualityComparator` with:
  - `Class<? extends EqualityComparator> value();`
- Change:
  - `Observable.equalityComparator()` default to `EqualityComparator.class`
  - `Memoize.equalityComparator()` default to `EqualityComparator.class`
- Processor must:
  - detect the sentinel default
  - derive comparators from the exact declared type annotation
  - validate comparator instantiability
  - preserve explicit override behavior
  - preserve observable accessor conflict diagnostics

## Quality Gates

- Targeted iteration checks:
  - `bundle exec buildr arez:processor:test`
  - `bundle exec buildr arez:integration-tests:test`
- Required full gates:
  - `bundle exec buildr test`
  - `bundle exec buildr test_api_diff`
  - `bundle exec buildr site:build`
- User-visible behavior requires `CHANGELOG.md` update.
- Docs must be updated in annotation Javadocs and `docs/`.

## Open Questions Register

| id | status | question | context | options | tradeoffs | recommended_default | user_decision | artifacts_updated |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Q-01 | resolved | Should type-level comparator lookup walk the hierarchy? | Hierarchy lookup improves reuse but requires precedence/conflict rules. | exact declared type only; walk supertypes/interfaces. | Exact-type lookup is predictable and keeps processor behavior easy to explain and test. | exact declared type only | exact declared type only | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-02 | resolved | Should v1 cover unannotatable types? | Arrays, JDK types, and third-party types cannot be annotated by downstream users. | annotation-only v1; add broader registry/fallback system. | Annotation-only v1 keeps API small and rollout safe, while explicit `equalityComparator` remains the escape hatch. | annotation-only v1 | annotation-only v1 | same |
