# Implementation Plan

## Phase Sequence

1. Create and maintain structured-delivery artifacts under `plans/reduce-managed-field-access/`.
2. Add processor warning ids and field-warning helper logic for the three managed field annotations.
3. Update processor fixtures and test coverage for warning, suppression, and inherited-protected scenarios.
4. Normalize existing fixtures that currently rely on discouraged field visibility.
5. Update docs and changelog.
6. Run verification gates and close out.

## Delivery Approach

- Keep the new behavior processor-local and field-specific rather than broadening generic access validation.
- Reuse the existing warning/suppression style already used for public/protected method guidance.
- Normalize pre-existing fixtures so each warning-focused test still produces only its intended warning.

## High-Risk Areas

- Risk 1: existing field fixtures already use `public` or same-package `protected` and will start warning unexpectedly.
  - Mitigation: explicitly normalize those fixtures or move them into warning-focused coverage.
- Risk 2: inherited `protected` fields may be over-warned if package comparison is done against the wrong owner type.
  - Mitigation: base the exception on the field’s declaring type versus the component type package relationship.
- Risk 3: warning tests may become flaky if a fixture emits multiple warnings.
  - Mitigation: keep dedicated single-warning fixtures for each new case and reduce visibility in unrelated fixtures.

## Required Full Gate

- `bundle exec buildr test`

## Decision Log

| question | concrete plan change |
| --- | --- |
| Q-01 | implement access guidance only for field entry points and leave method handling unchanged |
| Q-02 | add suppressable warnings and warning-fixture coverage rather than new failed-compile cases |

## Completion Criteria

- New `Arez:PublicField` and `Arez:ProtectedField` warnings exist and are suppressable.
- `@AutoObserve`, `@CascadeDispose`, and `@ComponentDependency` field processing emits the correct warnings.
- Same-package `protected` fields warn, inherited cross-package `protected` fields do not.
- Existing processor tests are updated to keep warning counts stable.
- Docs and changelog describe the final behavior.
