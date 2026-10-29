# T01 — Generate identity-preserving mapped relationships

- Status: pending
- Blocked by: None
- Spec coverage: R1,R2,R3,R4,R7,R8,R9; AC1,AC2,AC4 (source/factory and base lifecycle portions)

## Delivers

A generated zero-argument List relationship reconciles a conventional or explicitly named source/factory pair, with stable source identity, guarded result equality, normal memoization controls and observation-scoped cleanup.

## Acceptance criteria

- [ ] Add standalone MappedRelationship with all nine Memoize parameters/defaults and Source/Factory companion annotations with required single String name; reject Memoize overlap and invalid main declarations.
- [ ] Infer propertyStream and createTargetType using compatible effective signatures, preserving JSpecify contracts; ignore unrelated overloads, reject ambiguity, and strictly validate explicit association.
- [ ] Reuse memoized scheduling, refs/hooks and disposal through generated computation bodies; make map cleanup mandatory even without collection wrapping.
- [ ] Cache by source identity, preserve duplicate occurrences/order, accept nullable source contracts, recreate disposed targets once per source per pass, and guard comparator equality by ordered target reference identity.
- [ ] Cover basic lifecycle, separate caches/readers, all copied options, inherited/compiled signatures, source/factory failures and diagnostic/golden behavior with real runtime and generated components.

## Validation

- Focused core reconciliation/comparator tests — identity, order, duplicates, failure and deactivation behavior.
- Mapped processor diagnostics/goldens and targeted integration suite — generated API and runtime behavior, including copied settings.
- Existing memoize processor family — verifies unchanged ordinary generation.
- Java/build formatting checks for touched files.

## Evidence

- Pending.
