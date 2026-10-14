# T11 — Migrate relationships, generics and remaining Arez scenarios

- Status: complete
- Blocked by: T09
- Spec coverage: R02, R04, R06, AC02, AC04, AC06

## Delivers

Relationship/generic/inheritance behavior and the remaining Arez processor scenarios pass after the hard cut.

## Acceptance criteria

- [x] Own all original Arez families not assigned to T10, including inverse/reference, component_dependency, cascade_dispose, auto_observe, identity/ref access, inheritance/override_generics/type parameters, Sting, access/misplaced/duplicate annotation cases and warnings.
- [x] Preserve ReferenceId-derived nullable behavior, inverse cardinality and disposal semantics; resolve original declaration scope and substituted generic types.
- [x] Retain multi-input/output, formatted output, deprecated types, warning promotion, unresolved/two-phase and forked-javac scenarios.
- [x] Regenerate only affected paired goldens; retain a complete case mapping so the complement of T10 covers the original suite.
- [x] Document any current-nullness diagnostic replacement in Unreleased notes; do not delete valid behavior to simplify generation.

## Validation

- Run every grouped target owning this complementary family set and its diagnostic/warning/forked-javac targets; record exact labels.
- Run the generic/compiled nullability matrix and verify inverse/reference/disposal runtime contracts.
- bazel test //processor:all_tests --test_output=errors — full Arez processor gate must pass once T10 is also complete.
- Inventory union T10+T11 must cover every retained Arez case exactly once.

## Evidence

- Migrated the complementary 625 original input sources. [Source membership](../T11-fixture-membership.json) and [case inventory](../T11-case-inventory.json) prove that T10/T11 are disjoint, cover all 1,254 original inputs and retain all 463 golden and 677 diagnostic cases exactly once.
- `bazel test //processor:all_tests --test_output=errors` passed all 21 targets: 480 positive golden invocations in both modes (463 original plus 17 JSpecify cases), 677 original diagnostics, six focused JSpecify diagnostics and the forked-javac check. No skips. The complement targets are `component_goldens`, `action_goldens`, `observe_goldens`, `lifecycle_goldens`, `relationships_goldens`, `metadata_goldens`, `interop_goldens`, `scenarios_goldens` and `remaining_diagnostics` under `//processor/src/test/java/arez/processor:`.
- Collection inverse targets now use declared element identities rather than annotated printed names. List initialization retains ArrayList semantics; nullable nested inverse parameters carry type-use annotations on the type rather than the declaration.
- New inherited reference-ID regressions first failed with unresolved generated `T`. Descriptors now retain resolved ID types and original declaration scope, including component generic bindings. Non-null bindings omit null-ID branches; nullable bindings retain them. Source and Java 17 compiled bases match the same paired goldens, including parameter metadata and loaded type annotations.
- Retained old conflict/required-contract cases as JSpecify conflict and genuinely unspecified cardinality/managed-field diagnostics. Updated annotated type descriptions and SET_NULL diagnostics without adding legacy rejection. Only deliberate ignored-legacy fixtures retain javax nullability annotations.
- [Contract comparison](../T11-contract-review.json) verifies unchanged constructor signatures and non-null guard counts in all 251 changed original unformatted goldens. Reviewed inverse multiplicity, ReferenceId-derived null branches, disposal hooks and list/set construction. Both modes and exact output sets pass.
- Repeated all nine complementary/focused golden updaters; hashes of all 1,040 files in both expected trees stayed unchanged. Formatting, Buildifier, whitespace, documentation links and plan validation passed.
- The full JVM migration gate remains pending only for Persist (T12) and other owned generated consumers (T13); no suite was dropped.
