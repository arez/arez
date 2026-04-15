# ArezComponentLike Rename Requirements

## Mission

- Hard rename the public component-like marker annotation to `@arez.annotations.ArezComponentLike` without changing its behavior-neutral semantics.

## Scope Boundaries

- In scope: public annotation/source-file rename, processor and persist-processor terminology updates, diagnostics/Javadocs/docs refresh, test and fixture renames, expected-output renames, api-diff metadata updates, changelog updates, and structured workflow artifacts.
- Out of scope: compatibility aliases, retaining the former annotation name as deprecated API, and any change to `@ArezComponent` code generation semantics.

## Locked Decisions

- `@ArezComponentLike` remains a marker/exemption type rather than a processable `@ArezComponent` entrypoint.
- Covered Arez annotations inside an `@ArezComponentLike` type remain valid for processor validation and do not trigger misplaced-usage diagnostics.
- Type-level `@SuppressArezWarnings` remains valid only when the annotated type itself is annotated with `@ArezComponent` or `@ArezComponentLike`.
- `@AutoObserve(validateTypeAtRuntime = true)` remains valid only when the declared type is annotated with `@ArezComponentLike`.
- `@ComponentDependency` compatibility checks and unmanaged component-reference warnings continue accepting component-like targets.
- Public Javadocs/docs must describe `@ArezComponentLike` as a component-like validation host/target, not as a full component-processing annotation.
- Historical changelog entries may retain the former annotation name when they are intentionally describing older releases; all current docs and active workflow artifacts should use `@ArezComponentLike`.

## Command Surface

- Rename the component-like annotation source file to `core/src/main/java/arez/annotations/ArezComponentLike.java` and update the public type name.
- Rename processor/persist-processor constants and helper names so internal terminology consistently uses `ArezComponentLike`/component-like language.
- Rename impacted test classes, fixture files, expected outputs, and fixture class names that still embed the former annotation terminology.
- Refresh docs/Javadocs/changelog/api-diff metadata to match the new public API name and semantics.

## Quality Gates

- Targeted iteration checks:
  - `bundle exec buildr arez:processor:test`
- Required full gate:
  - `bundle exec buildr test`
- User-visible API/doc changes require `CHANGELOG.md` updates.
- Workflow artifacts under `plans/arez-component-like-rename/` must record verification evidence and any unrelated gate failures.

## Open Questions Register

| id | status | question | context | options | tradeoffs | recommended_default | user_decision | artifacts_updated |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| none | resolved | No open questions remain. | The user specified the rename semantics, scope, and verification bar in detail. | none | Additional questions would not materially reduce implementation risk. | proceed with the stated requirements | proceed with the stated requirements | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
