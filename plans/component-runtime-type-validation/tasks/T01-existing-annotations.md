# T01 — Align existing annotations

- Status: pending
- Blocked by: None
- Spec coverage: R1, R2, R4, R6 (existing annotations)

## Delivers

AutoObserve and ComponentDependency accept statically compatible types and warn
for unnecessary runtime opt-in. Uncertain component-like declarations require
explicit runtime opt-in; unrelated types fail with either flag. Preserve the
current runtime mechanisms and migrate affected fixtures to eligible types.

## Acceptance and validation

- Processor tests cover both flags, direct/generated capability, eligible abstract
  and interface types, ActAs eligibility, Object/DTO rejection, fields/methods,
  inherited declarations, and warning suppression.
- Existing generated output and real-component runtime suites pass after migration.
- API documentation and guides explain the policy and migration.
- Run processor all_tests and integration-tests all_tests; update affected goldens
  through the repository tool and inspect their changes.

## Evidence

Pending.
