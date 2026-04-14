# Reduce Managed Field Access Requirements

## Mission

- Add suppressable processor warnings that push `@AutoObserve`, `@CascadeDispose`, and `@ComponentDependency` field targets toward reduced visibility.

## Scope Boundaries

- In scope: field-target warning behavior in the processor, processor fixtures/tests, warning suppression coverage, docs, changelog, and structured-delivery artifacts.
- Out of scope: method-target access policy changes, hard validation changes, generated-code semantics, or broader access-rule refactors across unrelated annotations.

## Locked Decisions

- Scope is field targets only for `@AutoObserve`, `@CascadeDispose`, and `@ComponentDependency`.
- Enforcement is warning-level and suppressable, not a compile failure.
- New warning ids are `Arez:PublicField` and `Arez:ProtectedField`.
- `public` fields for these annotations always warn.
- `protected` fields for these annotations warn unless the field is inherited from a parent class in a different package than the `@ArezComponent` type.
- Existing hard checks remain unchanged for `private`, `static`, finality, package-access across packages, and type compatibility.
- Existing method-target behavior for these annotations remains unchanged.

## Command Surface

- Add two new warning ids to the processor constants.
- Extend field processing for:
  - `processAutoObserveField(...)`
  - `processCascadeDisposeField(...)`
  - `createFieldDependencyDescriptor(...)`
- Warning behavior:
  - emit an annotation-specific `should not be public` warning for `public` fields
  - emit an annotation-specific `should not declare a protected field` warning for unnecessary `protected` fields
- Suppression behavior must work with both `@SuppressWarnings` and `@SuppressArezWarnings`.

## Quality Gates

- Targeted iteration checks:
  - `bundle exec buildr arez:processor:test`
- Required full gate:
  - `bundle exec buildr test`
- User-visible behavior requires updates to `CHANGELOG.md` and the relevant annotation docs.

## Open Questions Register

| id | status | question | context | options | tradeoffs | recommended_default | user_decision | artifacts_updated |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Q-01 | resolved | Should this rule apply only to field targets or also to methods? | The annotations support both fields and methods, but the request named fields explicitly. | fields only; fields and methods. | Fields only matches the request and limits behavior churn. Extending to methods would broaden semantics and tests unnecessarily. | fields only | fields only | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-02 | resolved | Should the new rule warn or fail compilation? | Similar Arez access-reduction guidance is usually warning-level rather than a hard error. | warning; compile failure. | Warnings preserve compatibility while still guiding callers. Hard failures would be a breaking tightening. | warning | warning | same |
