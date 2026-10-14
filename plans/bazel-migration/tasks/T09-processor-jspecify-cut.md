# T09 — Cut both processors to the pinned Proton and JSpecify API

- Status: complete
- Blocked by: T08
- Spec coverage: R02, R03, R04, R06, AC02, AC03, AC04, AC06

## Delivers

Both processor products compile against the selected Proton pin, and a focused end-to-end contract suite proves the new interpretation.

## Acceptance criteria

- [x] Replace old Proton core/QA with the same source-module pin used by Sting. Switch test/example Sting processing to a compatible immutable migrated revision; preserve fixture feature intent.
- [x] Migrate processor support and generation to canonical JSpecify names and Proton's annotation-preserving TypeMirror/override APIs.
- [x] Implement the minimum effective-nullness interpretation needed by descriptors/generation, honoring declaration scopes, NullUnmarked, substitution and genuinely unspecified contracts.
- [x] Legacy nullability annotations have no effect; add no dedicated rejection diagnostic, scan or compatibility helper.
- [x] Add focused processor fixtures for marked/unmarked scopes, nullable arrays/elements, type variables, inherited generic substitutions and compiled-base contracts.
- [x] Preserve Arez constructor/initializer/guard/inverse/reference/disposal intent and Persist skip-null storage behavior; general fixture conversion belongs to T10-T13.
- [x] Do not globally suppress new failures or regenerate all old fixtures blindly. Inventory each outstanding family/consumer and preserve full-suite membership.

## Validation

- bazel build //processor //persist/processor — verifies the atomic product API cut.
- bazel test //processor:jspecify_contracts --test_output=errors — exercises new behavior through actual processor compilation/generation.
- Run a compiled-base two-stage fixture and a legacy-decorated marked fixture; output/nullness must follow only JSpecify.
- Record full-suite failures attributable to pending T10-T13 migrations; user-facing diagnostics and the JDK runtime change get Unreleased entries.

## Evidence

- Both processor products build against immutable Proton 6d50db451021b7880097d0880907aa036a30a689. Sting test/example dependencies use its immutable fc89bc7c3f4450d3d1b75eaa58e63edfb37f1267 public artifacts/plugin. Old Maven Proton/Sting dependencies are removed.
- Owned processor/support packages use JSpecify. Generation uses Proton's preserving mirror/override APIs, marked output and distinct top-level nullable cache/reference types. Nullness resolution uses canonical names only, original lexical/package scopes and inherited argument-binding scopes; it diagnoses unspecified/parametric initialization contracts while preserving primitive defaults.
- `bazel test //processor:jspecify_contracts //third_party/java:verify_config_sha256 --test_output=errors` passed: 14 positive compilations in both modes plus six diagnostics, no skips. [Inventory](../jspecify-contract-inventory.json) owns new inputs and all 26 golden files.
- Matrix includes nullable arrays/elements, type variables, NonNull operators, inherited nullable/non-null substitutions, intermediate binding defaults, unmarked argument/declaration scopes, package defaults/NullUnmarked overrides, conflicts and ignored javax/JetBrains annotations. A Java 17 compiled-base pass on the Java 21 QA compiler matches source generation; parameter metadata is retained and type-annotation symbol loading is explicit.
- Focused JSpecify regeneration passed repeatedly; a recorded hash comparison confirms all 26 files unchanged by the final repeat. Existing golden content is untouched. Persist keeps its skip-null/zero branches and compares Java type identity without nullability annotations.
- Full JVM run intentionally fails at the migration boundary: representative Arez component/action/lifecycle cases report unspecified contracts or differ from old goldens (T10/T11); Persist core Scope cannot generate from legacy contracts (T12), which prevents the aggregate from running every later suite. Remaining families/targets are retained. Extras/integrations/docs are owned by T13; the full JVM gate must be restored there.
- Both products, focused matrix, dependency checksum, formatting, Buildifier, Bash syntax and whitespace checks passed. Unreleased notes describe the processor runtime and interpretation cuts.
