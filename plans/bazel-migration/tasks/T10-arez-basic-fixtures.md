# T10 — Migrate component, observable and lifecycle scenarios

- Status: pending
- Blocked by: T09
- Spec coverage: R02, R04, R06, AC02, AC04, AC06

## Delivers

Basic components, observables, actions, memoization, observation and lifecycle families preserve behavior under JSpecify.

## Acceptance criteria

- [ ] Own the top-level com.example models and families action, component, observable, collections, memoize, memoize_context_parameter, observe, observer_ref, computable_value_ref, observable_value_ref, on_activate, on_deactivate, on_deps_change, post_construct, pre_dispose, post_dispose, requires_transaction and to_string.
- [ ] Migrate positive and associated negative fixtures by intended contracts; annotate permissive setters/fields explicitly rather than changing constructor requirements.
- [ ] Regenerate the affected formatted/unformatted goldens and update only diagnostics changed by the current JSpecify contract.
- [ ] Replace obsolete explicit javax Nonnull/Nullable conflict/required-annotation tests with meaningful current nullness tests. Legacy presence alone never causes an error.
- [ ] Retain case inventory, exact output checks, warning promotion and special methods within these families.

## Validation

- Run every grouped target owning the listed families, both golden modes and associated diagnostics; record the exact labels in evidence.
- Check initializer autodetection, setter guards, nullable collections and lifecycle output against equivalent prior contracts.
- Repeat focused golden regeneration; no further diff is allowed.

## Evidence

- pending
