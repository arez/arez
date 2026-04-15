# Misplaced Annotation Validation Requirements

## Mission

- Detect processor-consumed Arez annotations that appear outside valid Arez container types and fail compilation instead of silently ignoring them.

## Scope Boundaries

- In scope: processor discovery updates, misplaced-usage validation, comprehensive processor test fixtures for every covered annotation/placement, changelog, affected annotation Javadocs, and structured workflow artifacts.
- Out of scope: making `@ArezComponentLike` types processable Arez components and changing existing `@ArezComponent` code generation semantics.

## Locked Decisions

- A covered annotation is misplaced when its nearest enclosing type is annotated with neither `@ArezComponent` nor `@ArezComponentLike`.
- `@ArezComponentLike` remains a pure marker/exemption type in this change.
- Covered annotations inside `@ArezComponentLike` types remain ignored by normal processing and must not trigger the new misplaced-usage error.
- The validation covers processor-consumed Arez annotations that are misleading when ignored, including `@SuppressArezWarnings`.
- Type-level `@SuppressArezWarnings` is only valid when the annotated type itself is annotated with `@ArezComponent` or `@ArezComponentLike`; inherited-member exemptions do not apply to the type annotation.
- The validation excludes support/meta annotations `@ArezComponent`, `@ArezComponentLike`, and `@DefaultEqualityComparator`.
- Diagnostics should identify the annotation and state that it is only supported within a type annotated by `@ArezComponent` or `@ArezComponentLike`.

## Command Surface

- Expand `@SupportedAnnotationTypes` so the processor is invoked when any covered annotation appears in source.
- Add a round-level validation pass before normal `@ArezComponent` processing that:
  - scans elements annotated with covered annotations,
  - resolves the nearest enclosing `TypeElement`,
  - permits the annotation only when the type has `@ArezComponent` or `@ArezComponentLike`,
  - emits an error otherwise.

## Quality Gates

- Targeted iteration checks:
  - `bundle exec buildr arez:processor:test`
- Required full gate:
  - `bundle exec buildr test`
- User-visible behavior requires `CHANGELOG.md` updates.
- Affected annotation Javadocs should document the new compile-time validation.

## Open Questions Register

| id | status | question | context | options | tradeoffs | recommended_default | user_decision | artifacts_updated |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Q-01 | resolved | Should `@ArezComponentLike` become a real processing entrypoint? | The validation could either keep it as a marker or make its enclosed Arez annotations active. | Keep behavior-neutral; process like `@ArezComponent`. | Keeping it behavior-neutral avoids an API/semantic expansion and limits this change to diagnostics. | Keep behavior-neutral | Keep behavior-neutral | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-02 | resolved | Should the public marker annotation remain behavior-neutral? | The component-like marker name is clearer once misplaced annotations fail elsewhere. | Keep behavior-neutral; process like `@ArezComponent`. | Keeping it behavior-neutral avoids an API/semantic expansion and limits this change to diagnostics. | Keep behavior-neutral | Keep behavior-neutral | same |
| Q-03 | resolved | Which processor-known annotations should the scan cover? | Not every processor-known annotation is a silently ignored behavior annotation. | Broad misleading set; everything including support/meta annotations; minimal subset. | Broad misleading coverage catches the real silent-noop cases without overreaching into type markers such as `@DefaultEqualityComparator`. | Broad misleading coverage with explicit exclusions | Broad misleading coverage with explicit exclusions, including `@SuppressArezWarnings` and excluding `@ArezComponent`, `@ArezComponentLike`, `@DefaultEqualityComparator` | same |
