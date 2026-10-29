# T02 — Add reactive presentation and absent-target retention

- Status: complete
- Blocked by: T01
- Spec coverage: R3,R4,R5,R6,R7,R9; AC2,AC3 (optional callbacks), AC1 (retention lifecycle)

## Delivers

A mapped relationship filters and sorts targets reactively and retains live absent-source targets while its predicate requests retention.

## Acceptance criteria

- [x] Add Filter/Comparator/Retention companion annotations with required single String name and infer filterTargetType/propertyComparator/retainTargetType methods.
- [x] Resolve only compatible signatures; test optional wrong-signature overloads, explicit invalid bindings, duplicate valid candidates and shared implicit versus single-property explicit callbacks.
- [x] Keep hidden targets cached/observed; apply comparator provider per calculation; observe predicate/comparator reactive dependencies.
- [x] Retain live absent targets in previous encounter order after current sources, then apply presentation filtering and sorting; release when retention becomes false and avoid invoking predicates on disposed values.
- [x] Demonstrate source disposal with SET_NULL/snapshot targets, source reappearance, retained target disposal, independent relationships and deactivation/owner cleanup in both wrapping modes.
- [x] Exercise source prefiltering, changing target visibility/sort direction, and filter/comparator/retention failure recovery using real components.

## Validation

- Focused runtime, mapped processor family and integration scenarios.
- Ordinary memoize/relationship regression tests where shared generation paths changed.
- Review exact generated-file sets in both golden modes; format maintained sources and BUILD files.

## Evidence

- `bazel test //core:tests //integration-tests:tests ...:mapped_relationship_diagnostics ...:mapped_relationship_goldens` passed: 898 core cases, 230 integration cases (30 mapped cases), 38 diagnostic cases, 7 golden cases including both formatting modes and compiled inherited generic signatures.
- Shared-path regressions passed: memoize (40), relationships (77), JSpecify goldens (21), JSpecify diagnostics (7), nullability (1).
- The overload fixture first failed with a plain method reference selecting a more-specific wrong-return overload; explicit selected-parameter calls now compile under strict nullness checks for factories and both predicates.
- Real stream failure coverage includes a new target created before a later factory failure, followed by deactivation; no returned or unpublished targets leak observation.
- Java formatter write, buildifier and `git diff --check` passed. Maintained BUILD entries and exact generated sets reviewed.
