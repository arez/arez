# Lifecycle Sequencing Tests Requirements

## Mission

- Add integration tests that verify the runtime ordering of Arez lifecycle callbacks with a practical but representative subset of inheritance and observer scenarios.

## Scope Boundaries

- In scope: `integration-tests` coverage for lifecycle callback ordering, structured-delivery artifacts.
- Out of scope: processor fixture expansion, runtime behavior changes, documentation updates, changelog updates.

## Locked Decisions

- Coverage will be implemented as integration tests because the goal is runtime sequencing rather than generated-source shape.
- The subset will cover:
  - duplicate-capable callbacks: `@PostConstruct`, `@PreDispose`, and `@PostDispose`
  - single-instance observer lifecycle hooks: `@OnActivate`, `@OnDeactivate`, and `@OnDepsChange`
- The duplicate-capable scenario must mix methods sourced from:
  - a grandparent class
  - a parent class
  - an interface implemented by a superclass
  - an interface implemented directly by the component
- Assertions will use an appended trace joined into a text block so the full sequence is visible in a single failure.

## Command Surface

- Add or extend integration tests under `integration-tests/src/test/java/arez/integration/lifecycle/`.
- Reuse nested test components/interfaces inside the integration test file unless extraction is needed for access constraints.

## Quality Gates

- Targeted iteration gate:
  - `bundle exec buildr arez:integration-tests:test`
- If the targeted gate exposes unrelated failures, capture that in task evidence.

## Open Questions Register

- No open questions at present. The current subset follows existing integration-test conventions and the user request for a reasonable representative matrix.
