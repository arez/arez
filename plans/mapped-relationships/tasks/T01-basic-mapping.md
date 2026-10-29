# T01 — Generate identity-preserving mapped relationships

- Status: complete
- Blocked by: None
- Spec coverage: R1,R2,R3,R4,R7,R8,R9; AC1,AC2,AC4 (source/factory and base lifecycle portions)

## Delivers

A generated zero-argument List relationship reconciles a conventional or explicitly named source/factory pair, with stable source identity, guarded result equality, normal memoization controls and observation-scoped cleanup.

## Acceptance criteria

- [x] Add standalone MappedRelationship with all nine Memoize parameters/defaults and Source/Factory companion annotations with required single String name; reject Memoize overlap and invalid main declarations.
- [x] Infer propertyStream and createTargetType using compatible effective signatures, preserving JSpecify contracts; ignore unrelated overloads, reject ambiguity, and strictly validate explicit association.
- [x] Reuse memoized scheduling, refs/hooks and disposal through generated computation bodies; make map cleanup mandatory even without collection wrapping.
- [x] Cache by source identity, preserve duplicate occurrences/order, accept nullable source contracts, recreate disposed targets once per source per pass, and guard comparator equality by ordered target reference identity.
- [x] Cover basic lifecycle, separate caches/readers, all copied options, inherited/compiled signatures, source/factory failures and diagnostic/golden behavior with real runtime and generated components.

## Validation

- Focused core reconciliation/comparator tests — identity, order, duplicates, failure and deactivation behavior.
- Mapped processor diagnostics/goldens and targeted integration suite — generated API and runtime behavior, including copied settings.
- Existing memoize processor family — verifies unchanged ordinary generation.
- Java/build formatting checks for touched files.

## Evidence

- Passed: `bazel test //core:tests //integration-tests:tests //processor/src/test/java/arez/processor:mapped_relationship_diagnostics //processor/src/test/java/arez/processor:mapped_relationship_goldens //processor/src/test/java/arez/processor:memoize_goldens --test_output=errors --noshow_progress` with pinned Bazel 9.3.1.
- Actual TestNG logs: core 898/898; integration 216/216 (16 mapped cases); mapped diagnostics 22/22; mapped goldens 5/5, including source/compiled generic-base parity in both formatting modes. Existing memoize goldens passed.
- Passed maintained Java format check, buildifier check and `git diff --check`. Reviewed generated output and source changes; ordinary memoize goldens unchanged.
- Nullable-source integration uses a real observable revision plus a source stream, avoiding dependence on the existing nullable-container property-introspection inference issue encountered during fixture construction. Mapping nullness is checked in generated cache types, method references, diagnostics and inherited compiled signatures.
