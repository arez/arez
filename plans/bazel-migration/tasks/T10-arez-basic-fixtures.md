# T10 — Migrate component, observable and lifecycle scenarios

- Status: complete
- Blocked by: T09
- Spec coverage: R02, R04, R06, AC02, AC04, AC06

## Delivers

Basic components, observables, actions, memoization, observation and lifecycle families preserve behavior under JSpecify.

## Acceptance criteria

- [x] Own the top-level com.example models and families action, component, observable, collections, memoize, memoize_context_parameter, observe, observer_ref, computable_value_ref, observable_value_ref, on_activate, on_deactivate, on_deps_change, post_construct, pre_dispose, post_dispose, requires_transaction and to_string.
- [x] Migrate positive and associated negative fixtures by intended contracts; annotate permissive setters/fields explicitly rather than changing constructor requirements.
- [x] Regenerate the affected formatted/unformatted goldens and update only diagnostics changed by the current JSpecify contract.
- [x] Replace obsolete explicit javax Nonnull/Nullable conflict/required-annotation tests with meaningful current nullness tests. Legacy presence alone never causes an error.
- [x] Retain case inventory, exact output checks, warning promotion and special methods within these families.

## Validation

- Run every grouped target owning the listed families, both golden modes and associated diagnostics; record the exact labels in evidence.
- Check initializer autodetection, setter guards, nullable collections and lifecycle output against equivalent prior contracts.
- Repeat focused golden regeneration; no further diff is allowed.

## Evidence

- Migrated 629 owned positive, negative and unresolved input sources to explicit JSpecify contracts, preserving permissive reference fields/setters and nullable type-variable bounds. Existing explicit non-null contracts retain constructor requirements.
- Split mixed providers and special-method selections into basic/complement targets. [Case inventory](../T10-case-inventory.json) proves the same 463 retained golden cases plus 14 T09 positive cases and all 677 original diagnostic cases occur exactly once. [Partition](../T10-test-partition.json) and [source membership](../T10-fixture-membership.json) record ownership.
- `bazel test` passed these labels under `//processor/src/test/java/arez/processor:`: `basic_component_goldens`, `basic_action_goldens`, `observable_goldens`, `memoize_goldens`, `basic_observe_goldens`, `basic_lifecycle_goldens`, `basic_metadata_goldens`, `basic_scenarios_goldens`, and `basic_diagnostics`; `//processor:jspecify_contracts` also passed. This covers 250 golden invocations in both modes, 328 diagnostics and the focused 20-case matrix, with no skips.
- Corrected hook discovery/validation to compare declared Observer/ComputableValue identities and Object casts to compare unannotated Java types. Existing annotated hook fixtures reproduce these failures and now pass. Diagnostics retain their original invalid behavior while describing nullable initializer contracts and annotated context types.
- [Generated contract comparison](../T10-contract-review.json) checked every changed unformatted golden: all 254 retain their prior constructor signatures and non-null guard counts. Reviewed nullable collections, initializer defaults and lifecycle bodies; paired output and exact-file assertions passed.
- Repeated all eight focused golden updaters; hashes of every existing golden in both trees were unchanged. Java formatting, Buildifier, Bash syntax, whitespace, plan and documentation-link checks passed.
- The full JVM milestone remains intentionally pending: unmigrated relationship/configuration/interop/inheritance/diagnostic fixtures belong to T11; Persist contracts belong to T12; other generated consumers belong to T13. Aggregate membership is retained.
