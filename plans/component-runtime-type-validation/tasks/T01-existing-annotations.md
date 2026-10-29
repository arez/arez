# T01 — Align existing annotations

- Status: complete
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

- Processor matrix and integration checks passed.
- `tools/update_goldens.sh arez relationships` and `tools/update_goldens.sh arez scenarios` passed; reviewed both formatting modes.
- `bazel test //processor:all_tests //integration-tests:tests --test_output=errors`: 26 targets passed.
- `tools/java_format.sh write` applied; document links and diff whitespace checked.
- Compiled generic-base tests exposed unresolved type variables in validation and generated locals; concrete member resolution fixed and verified.
- Unnecessary generated AutoObserve checks removed for proven types; strict generated-code checks pass.
- Duplicate dependency parsing suppressed so each redundant setting emits one warning.
