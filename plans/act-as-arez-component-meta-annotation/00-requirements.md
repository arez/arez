# ActAsArezComponent Meta-Annotation Requirements

## Mission

- Allow downstream frameworks to mark their own type annotations as Arez-compatible containers so covered Arez annotations can appear on those framework-managed types without requiring a direct compile-time dependency on `arez.annotations.ActAsArezComponent`.

## Scope Boundaries

- In scope: processor detection for component-like containers, support for simple-name fallback meta-annotations named `ActAsArezComponent`, processor fixtures/tests, `@ActAsArezComponent` Javadocs alignment, `CHANGELOG.md`, and structured workflow artifacts.
- Out of scope: making custom framework annotations behave like `@ArezComponent` for code generation, inheriting any configurable `@ArezComponent` parameters through meta-annotations, or changing runtime behavior.

## Locked Decisions

- A type remains a real Arez component only when directly annotated with `@ArezComponent`.
- A type should be treated as Arez-component-like when it is either:
  - directly annotated with `@ArezComponentLike`, or
  - annotated with any annotation whose own annotation type is named `ActAsArezComponent`, regardless of package.
- The simple-name fallback must work even when the downstream `ActAsArezComponent` annotation is package-access.
- The new meta-annotation support is compatibility-oriented and must not require downstream frameworks to import or depend on the Arez annotation type.
- Diagnostics and validation paths that currently talk about `@ArezComponentLike` should remain behaviorally coherent when the custom meta-annotation path is used.

## Command Surface

- Update processor container detection so misplaced-annotation validation accepts framework annotations marked via `ActAsArezComponent`.
- Update processor “component-like” checks used by annotation validation and unmanaged reference analysis to recognize the same meta-annotation rule.
- Add positive fixtures proving successful compilation for:
  - the direct `arez.annotations.ActAsArezComponent` path,
  - a same-simple-name fallback annotation in another package, including package-access visibility.

## Quality Gates

- Targeted iteration checks:
  - `bundle exec buildr arez:processor:test`
- Required full gate:
  - `bundle exec buildr test`
- User-visible behavior requires `CHANGELOG.md` updates.
- `@ActAsArezComponent` Javadocs should describe both the preferred direct-annotation path and the simple-name compatibility fallback.

## Open Questions Register

| id | status | question | context | options | tradeoffs | recommended_default | user_decision | artifacts_updated |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Q-01 | resolved | How broad should the downstream compatibility match be for meta-annotations? | Frameworks may want to avoid a direct Arez dependency and may place a package-access `ActAsArezComponent` marker in their own package. | exact Arez type only; exact type plus limited aliases; any meta-annotation whose simple name is `ActAsArezComponent` | Exact-type matching is safer but fails the stated integration goal. Simple-name matching is broader and could theoretically match an unrelated annotation, but it directly enables the intended zero-dependency framework pattern. | any meta-annotation whose simple name is `ActAsArezComponent` | any meta-annotation whose simple name is `ActAsArezComponent`, regardless of package | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
