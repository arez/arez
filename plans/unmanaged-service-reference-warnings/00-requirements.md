# Unmanaged Service Reference Warnings Requirements

## Mission

- Update unmanaged component-reference warnings so constructor injection no longer suppresses field warnings, while treating service-style component references as a separate non-private field warning.

## Scope Boundaries

- In scope: processor field warning logic, warning ids/messages, processor fixtures/tests, changelog, and structured-delivery artifacts.
- Out of scope: method-return visibility warnings, runtime generation changes, non-field injection behavior, or broader access-policy refactors beyond the requested service-field rule.

## Locked Decisions

- Remove the Sting constructor-injection exemption from unmanaged field-reference warnings.
- Unmanaged field-reference warnings for `@ArezComponent` targets must use effective `disposeNotifier` resolution rather than explicit syntax only.
- `@ArezComponent(service = ENABLE)` field types with effective `disposeNotifier = DISABLE` must skip unmanaged-reference warnings.
- Add a new suppressable warning for non-`private` fields whose type is an `@ArezComponent` with resolved `service = ENABLE`.
- The new visibility rule is field-only, applies to all matching field elements encountered, and remains independent from `@ComponentDependency`, `@CascadeDispose`, and `@AutoObserve`.
- If both the unmanaged-reference rule and the service-visibility rule apply, emit both warnings.

## Command Surface

- Update `warnOnUnmanagedComponentReferences(...)` in the processor to remove constructor-injection suppression and refresh the warning text.
- Add the dedicated warning id `Arez:NonPrivateServiceField`.
- Emit the new warning on the field element using one generic “should be private” message that mentions the `@ArezComponent(service = ENABLE)` condition.
- Add processor fixture/test coverage for the updated unmanaged-reference behavior and the new service-field warning.
- Update `CHANGELOG.md` for the user-visible warning changes.

## Quality Gates

- Targeted iteration checks:
  - `bundle exec buildr arez:processor:test`
- Required full gate:
  - `bundle exec buildr test`

## Open Questions Register

| id | status | question | context | options | tradeoffs | recommended_default | user_decision | artifacts_updated |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Q-01 | resolved | Should the new “field must be private” behavior be a warning or a hard processor error? | The new service-field rule tightens access expectations but mirrors existing warning-style field guidance. | warning; error | Warning preserves compatibility and matches current warning patterns. Error would be a breaking behavioral tightening. | warning | warning | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-02 | resolved | Which referenced types should receive the new non-private field warning? | The request explicitly called out `@ArezComponent(service = ENABLE)` field types. | service-enabled `@ArezComponent` only; include `@ArezComponentLike`; include any `DisposeNotifier` | Narrow scope matches the request and avoids broad policy expansion. | service-enabled `@ArezComponent` only | service-enabled `@ArezComponent` only | same |
| Q-03 | resolved | Should package-private fields also warn? | “Not private” can either literally include package-private or only mirror existing public/protected warnings. | all non-`private`; only `public`/`protected` | Covering all non-`private` fields matches the requested policy and avoids a silent gap. | all non-`private` | all non-`private` | same |
| Q-04 | resolved | Do lifecycle annotations suppress the new non-private service-field warning? | A service field can also be annotated with `@ComponentDependency`, `@CascadeDispose`, or `@AutoObserve`. | still warn; suppress when lifecycle-managed | Visibility and lifecycle are separate concerns. Keeping the warning independent preserves clear guidance. | still warn | still warn | same |
| Q-05 | resolved | Should the new non-private warning apply only to fields declared on the processed component or to all matching field references encountered? | The field walk may include inherited fields. | declared fields only; all matching field elements encountered | All encountered fields matches the requested “all references” scope, though it broadens inherited coverage. | declared fields only | all matching field elements encountered | same |
| Q-06 | resolved | If a field is both unmanaged and non-private, should the processor emit one warning or both? | The unmanaged-reference and service-field rules target different concerns. | emit both; replace unmanaged warning with new warning | Separate warnings preserve suppression precision and explain both issues. | emit both | emit both | same |
| Q-07 | resolved | Should Sting-injected constructor/field type matches now warn? | Removing injection-based suppression changes current `UnmanagedComponentReferenceStingInjected` behavior. | yes; no | Warning aligns behavior with the requested policy and removes the special-case allowlist. | yes | yes | same |
| Q-08 | resolved | Should the unmanaged-reference skip for `disposeNotifier = DISABLE` use explicit syntax only or effective resolution? | Service components derive `disposeNotifier = DISABLE` by default. | explicit only; effective resolution | Effective resolution matches the requested semantics and existing helper behavior. | effective resolution | effective resolution | same |
| Q-09 | resolved | For service-enabled components with effective `disposeNotifier = DISABLE`, should only the new non-private warning remain? | These types are meant to skip unmanaged-reference warnings but still follow the private-field rule. | yes; still emit unmanaged warning | Keeping only the service-field warning makes the split between lifecycle and visibility coherent. | yes | yes | same |
| Q-10 | resolved | Should the new rule stay purely element-based? | Inherited/private patterns could inspire broader diagnostics, but the processor reasons on elements. | element-based only; broader inherited-exposure diagnostic | Element-based diagnostics remain actionable and suppressable without extra policy invention. | element-based only | element-based only | same |
| Q-11 | resolved | Should the new rule get a dedicated warning id? | Existing field-access warnings are shared by other annotations and do not cover package-private cases. | dedicated id; reuse `Arez:PublicField` / `Arez:ProtectedField` | A dedicated id keeps suppression clear and avoids overloading old semantics. | dedicated id | dedicated id | same |
| Q-12 | resolved | Should the new warning id split by visibility level? | The rule can either model one “must be private” concept or three access-specific cases. | one id; separate ids per visibility | One id keeps suppression, docs, and tests simpler. | one id | one id | same |
| Q-13 | resolved | How much rationale should the new warning message include? | The processor can either explain service/dispose defaults or stay concise. | mention `service = ENABLE` only; mention `service` and `disposeNotifier`; generic private warning only | Mentioning `service = ENABLE` explains applicability without exposing internal default-resolution details. | mention `service = ENABLE` only | mention the `@ArezComponent(service = ENABLE)` condition, but not the `disposeNotifier` rationale | same |
| Q-14 | resolved | Should the unmanaged-reference field warning text still mention constructor injection? | Injection no longer suppresses the warning. | remove the injection phrase; keep the old wording | Keeping the old phrase would be inaccurate once the exemption is removed. | remove the injection phrase | remove the injection phrase | same |
| Q-15 | resolved | Should there be a method-return analogue to the new service-field visibility rule? | The request targeted fields, but service types can also appear in abstract getters. | field-only; fields and methods | Field-only keeps scope tight and aligned with the encapsulation concern. | field-only | field-only | same |
| Q-16 | resolved | Where should the new warning be reported? | Warnings can be attached to either the field or the enclosing component. | field element; component type | Field-level reporting matches existing access-warning patterns and makes suppression precise. | field element | field element | same |
| Q-17 | resolved | Should the new warning message vary by actual visibility? | The rule could mention `public`, `protected`, or package-private, or use one generic message. | generic message; visibility-specific messages | One generic message keeps the rule and its tests straightforward. | generic message | generic message | same |
