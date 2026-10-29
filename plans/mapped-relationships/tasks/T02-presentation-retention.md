# T02 — Add reactive presentation and absent-target retention

- Status: pending
- Blocked by: T01
- Spec coverage: R3,R4,R5,R6,R7,R9; AC2,AC3 (optional callbacks), AC1 (retention lifecycle)

## Delivers

A mapped relationship filters and sorts targets reactively and retains live absent-source targets while its predicate requests retention.

## Acceptance criteria

- [ ] Add Filter/Comparator/Retention companion annotations with required single String name and infer filterTargetType/propertyComparator/retainTargetType methods.
- [ ] Resolve only compatible signatures; test optional wrong-signature overloads, explicit invalid bindings, duplicate valid candidates and shared implicit versus single-property explicit callbacks.
- [ ] Keep hidden targets cached/observed; apply comparator provider per calculation; observe predicate/comparator reactive dependencies.
- [ ] Retain live absent targets in previous encounter order after current sources, then apply presentation filtering and sorting; release when retention becomes false and avoid invoking predicates on disposed values.
- [ ] Demonstrate source disposal with SET_NULL/snapshot targets, source reappearance, retained target disposal, independent relationships and deactivation/owner cleanup in both wrapping modes.
- [ ] Exercise source prefiltering, changing target visibility/sort direction, and filter/comparator/retention failure recovery using real components.

## Validation

- Focused runtime, mapped processor family and integration scenarios.
- Ordinary memoize/relationship regression tests where shared generation paths changed.
- Review exact generated-file sets in both golden modes; format maintained sources and BUILD files.

## Evidence

- Pending.
