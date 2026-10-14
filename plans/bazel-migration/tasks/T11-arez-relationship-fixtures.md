# T11 — Migrate relationships, generics and remaining Arez scenarios

- Status: pending
- Blocked by: T09
- Spec coverage: R02, R04, R06, AC02, AC04, AC06

## Delivers

Relationship/generic/inheritance behavior and the remaining Arez processor scenarios pass after the hard cut.

## Acceptance criteria

- [ ] Own all original Arez families not assigned to T10, including inverse/reference, component_dependency, cascade_dispose, auto_observe, identity/ref access, inheritance/override_generics/type parameters, Sting, access/misplaced/duplicate annotation cases and warnings.
- [ ] Preserve ReferenceId-derived nullable behavior, inverse cardinality and disposal semantics; resolve original declaration scope and substituted generic types.
- [ ] Retain multi-input/output, formatted output, deprecated types, warning promotion, unresolved/two-phase and forked-javac scenarios.
- [ ] Regenerate only affected paired goldens; retain a complete case mapping so the complement of T10 covers the original suite.
- [ ] Document any current-nullness diagnostic replacement in Unreleased notes; do not delete valid behavior to simplify generation.

## Validation

- Run every grouped target owning this complementary family set and its diagnostic/warning/forked-javac targets; record exact labels.
- Run the generic/compiled nullability matrix and verify inverse/reference/disposal runtime contracts.
- bazel test //processor:all_tests --test_output=errors — full Arez processor gate must pass once T10 is also complete.
- Inventory union T10+T11 must cover every retained Arez case exactly once.

## Evidence

- pending
