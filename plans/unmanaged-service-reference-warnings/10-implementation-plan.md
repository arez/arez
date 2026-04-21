# Implementation Plan

## Phase Sequence

1. Record accepted requirements, decisions, and plan approval for the warning-behavior change.
2. Refactor processor field-warning classification for unmanaged references and service-field visibility.
3. Add/update processor fixtures and warning expectations for the new behavior.
4. Update `CHANGELOG.md` and run verification gates.

## Delivery Approach

- Keep the behavior changes localized to processor warning logic rather than broadening validation or generation semantics.
- Reuse existing feature-resolution helpers for `service` and `disposeNotifier` so the field behavior matches component defaults.
- Reuse the current warning/suppression patterns for field-level diagnostics while introducing a dedicated warning id for service-field privacy.

## High-Risk Areas

- Risk 1: field-type classification may diverge for inherited or generic fields if the warning logic uses raw field types instead of effective member types.
  - Mitigation: reuse the `asMemberOf(...)` pattern already used by field validation paths.
- Risk 2: removing the injection exemption changes existing warning counts and messages.
  - Mitigation: update warning fixtures/tests explicitly, including the current Sting-injected no-warning case.
- Risk 3: the new service-field rule could accidentally warn on non-service components if `service` resolution is not applied consistently.
  - Mitigation: centralize the `service = ENABLE` decision in a small helper that only accepts `@ArezComponent` targets.

## Required Full Gate

- `bundle exec buildr test`

## Decision Log

| question | concrete plan change |
| --- | --- |
| Q-01 | implement `Arez:NonPrivateServiceField` as a suppressable warning, not an error |
| Q-02 | scope the new warning to `@ArezComponent` field types with resolved `service = ENABLE` |
| Q-03 | warn for all non-`private` service fields, including package-private |
| Q-04 | keep the service-field warning independent from `@ComponentDependency`, `@CascadeDispose`, and `@AutoObserve` |
| Q-05 | apply the service-field warning to every matching field element encountered by the processor field walk |
| Q-06 | allow unmanaged-reference and service-field warnings to co-exist on one field |
| Q-07 | remove the Sting constructor-injection exemption so current injected fixture coverage flips to warning coverage |
| Q-08 | use effective `disposeNotifier` resolution for unmanaged-reference skips |
| Q-09 | skip unmanaged-reference warnings for effective `disposeNotifier = DISABLE` components while still applying the service-field rule |
| Q-10 | keep diagnostics element-based rather than inventing inherited-exposure rules |
| Q-11 | add dedicated warning id `Arez:NonPrivateServiceField` |
| Q-12 | use a single warning id for all non-`private` cases |
| Q-13 | mention the `@ArezComponent(service = ENABLE)` condition in the new warning text without discussing `disposeNotifier` defaults |
| Q-14 | remove constructor-injection wording from unmanaged-reference field messages |
| Q-15 | keep the new warning field-only |
| Q-16 | report the new warning on the field element |
| Q-17 | use one generic “should be private” message across visibilities |

## Completion Criteria

- Unmanaged-reference field warnings no longer depend on constructor injection and no longer mention constructor injection in the message.
- Effective `disposeNotifier = DISABLE` component field types skip unmanaged-reference warnings.
- Non-`private` fields referencing `@ArezComponent(service = ENABLE)` emit `Arez:NonPrivateServiceField`.
- Processor tests cover the new warning combinations and suppressions.
- `CHANGELOG.md` documents the warning-behavior update.
