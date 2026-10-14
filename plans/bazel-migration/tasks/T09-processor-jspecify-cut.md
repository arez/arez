# T09 — Cut both processors to the pinned Proton and JSpecify API

- Status: pending
- Blocked by: T08
- Spec coverage: R02, R03, R04, R06, AC02, AC03, AC04, AC06

## Delivers

Both processor products compile against the selected Proton pin, and a focused end-to-end contract suite proves the new interpretation.

## Acceptance criteria

- [ ] Replace old Proton core/QA with the same source-module pin used by Sting. Switch test/example Sting processing to a compatible immutable migrated revision; preserve fixture feature intent.
- [ ] Migrate processor support and generation to canonical JSpecify names and Proton's annotation-preserving TypeMirror/override APIs.
- [ ] Implement the minimum effective-nullness interpretation needed by descriptors/generation, honoring declaration scopes, NullUnmarked, substitution and genuinely unspecified contracts.
- [ ] Legacy nullability annotations have no effect; add no dedicated rejection diagnostic, scan or compatibility helper.
- [ ] Add focused processor fixtures for marked/unmarked scopes, nullable arrays/elements, type variables, inherited generic substitutions and compiled-base contracts.
- [ ] Preserve Arez constructor/initializer/guard/inverse/reference/disposal intent and Persist skip-null storage behavior; general fixture conversion belongs to T10-T13.
- [ ] Do not globally suppress new failures or regenerate all old fixtures blindly. Inventory each outstanding family/consumer and preserve full-suite membership.

## Validation

- bazel build //processor //persist/processor — verifies the atomic product API cut.
- bazel test //processor:jspecify_contracts --test_output=errors — exercises new behavior through actual processor compilation/generation.
- Run a compiled-base two-stage fixture and a legacy-decorated marked fixture; output/nullness must follow only JSpecify.
- Record full-suite failures attributable to pending T10-T13 migrations; user-facing diagnostics and the JDK runtime change get Unreleased entries.

## Evidence

- pending
