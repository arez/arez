# Implementation Plan

## Phase Sequence

1. Create and maintain structured-delivery artifacts under `plans/lifecycle-sequencing-tests/`.
2. Add an inheritance-heavy integration scenario that validates duplicate callback ordering across classes and interfaces.
3. Add an observer-driven integration scenario that validates activation, deps-change, and deactivation ordering alongside normal component operations.
4. Run targeted integration tests and record evidence.

## Delivery Approach

- Keep the change limited to integration coverage.
- Prefer a single lifecycle-focused test class with nested helper types so the ordering trace remains easy to read.
- Mirror the processor’s existing inheritance ordering expectations for duplicate-capable callbacks while validating the actual runtime path.

## High-Risk Areas

- Risk 1: expected ordering may differ subtly between construction and disposal paths.
  - Mitigation: derive expectations from existing generated fixture coverage before writing assertions.
- Risk 2: observer-driven hooks may be noisy if mixed with too many unrelated behaviors.
  - Mitigation: keep the activation/deps-change scenario small and deterministic.

## Required Full Gates

- `bundle exec buildr arez:integration-tests:test`

## Decision Log

- Integration tests are the coverage vehicle because the requirement is runtime sequencing.
- The final subset is two scenarios: one for duplicate-capable callbacks, one for observer-driven lifecycle hooks.

## Completion Criteria

- Duplicate-capable lifecycle callbacks are validated with one end-to-end ordering assertion.
- Observer-driven lifecycle hooks are validated with one end-to-end ordering assertion.
- Targeted integration tests pass or any unrelated failure is documented.
