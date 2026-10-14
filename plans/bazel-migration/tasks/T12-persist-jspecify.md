# T12 — Restore the Persist slice after the nullability cut

- Status: complete
- Blocked by: T10, T11
- Spec coverage: R02, R04, R06, AC02, AC04, AC06

## Delivers

Persist runtime models, its processor fixtures and its integration-generated code pass with current contracts.

## Acceptance criteria

- [x] Migrate Persist core/test/integration sources and Persist QA inputs/output expectations to JSpecify by intent.
- [x] Preserve persistence skip-null/restore-present behavior, converters, web storage facades and public signatures.
- [x] Check inherited/compiled members without inventing stronger Persist storage invariants; preserve the additional Arez processor and filtered output sets.
- [x] Regenerate both Persist golden modes and retained diagnostic/warning/forked-javac scenarios.

## Validation

- bazel build //persist/core //persist/processor — verifies both product targets.
- bazel test //persist/core:tests //persist/processor:all_tests //persist/integration-tests:tests --test_output=errors — restores the full Persist slice.
- Run representative null-valued persistence/restore and multi-processor golden cases; compare storage behavior with the baseline.

## Evidence

- Migrated all 28 Persist main sources, 14 current test/support sources and eight integration sources into marked packages. Nullable converter outputs/type bounds and state-map values reflect existing behavior; converter inputs retain their explicit non-null operators. Native storage getItem/JSON parse results and generated restore locals express nullable contracts.
- [Source membership](../T12-source-membership.json) records the original 43 owned sources and 55 fixture inputs. [Case inventory](../T12-case-inventory.json) verifies every original provider row remains, with two inherited-property golden methods and one nullable-value integration scenario added.
- `bazel build //persist/core //persist/processor` and `bazel test //persist/core:tests //persist/processor:all_tests //persist/integration-tests:tests --test_output=errors` passed. This covers 29 runtime tests, 25 positive golden invocations in both modes, 31 diagnostics, forked javac and two integration scenarios, with no skips.
- Eight existing boxed-property scenarios first failed because annotation text entered converter identifiers. Converter names, deduplication and class literals now use Java type identity. Source and Java 17 compiled inherited nullable properties match the same paired golden, with explicit parameter metadata and loaded type annotations.
- The nullable-value runtime regression checks absent state, restored data, null-valued removal/skip behavior and later non-null saves. Existing null encode/decode passthrough and tree persistence scenarios pass.
- [Contract review](../T12-contract-review.json) compares all 231 method bodies across the 23 original unformatted goldens; runtime bodies are identical after removing nullability annotations for comparison. The restore-present/null and primitive-zero branches are preserved.
- Repeated both golden updaters; all 48 expected files kept identical hashes. Java formatting, Buildifier, whitespace, plan validation and documentation links pass. Direct product/test javax nullability dependencies are replaced with JSpecify; unrelated third-party tool metadata remains for T13's full audit.

- Known pre-existing issue for T26 knowledge classification: `SidecarGenerator.buildRestoreStateMethod` emits the property-key expression rather than `state.get(key)` for Object properties. The baseline Object golden proves this predates the migration. Preserve the existing branch here; a separate follow-up should add an Object round-trip regression and repair it. Nullable string storage/restore is verified by this task.

- T18 follow-up: its isolated compiled-generic consumer exposed original type-variable use in Persist storage/converter generation. T18 resolves member types in the concrete component context and adds paired source/compiled generic goldens; the original 25 invocations are retained.
