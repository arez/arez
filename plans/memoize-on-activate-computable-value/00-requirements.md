# Memoize OnActivate Requirements

## Mission

- Allow `@Memoize( depType = DepType.AREZ_OR_EXTERNAL )` to compile without a paired `@ComputableValueRef` when the associated `@OnActivate` hook already receives the `ComputableValue`.
- Add integration coverage proving the runtime path works when the component stores the `ComputableValue` from `@OnActivate` and uses it to signal external dependency changes.

## Scope Boundaries

- In scope: annotation-processor validation, generated hook wiring, processor regression fixtures, integration-test coverage for the `@OnActivate(ComputableValue)` external-dependency flow, changelog entry, structured-delivery artifacts.
- Out of scope: runtime `ComputableValue` behavior changes, docs beyond `CHANGELOG.md`, broader lifecycle hook signature changes.

## Locked Decisions

- The processor should continue rejecting `AREZ_OR_EXTERNAL` when neither a `@ComputableValueRef` nor an `@OnActivate(ComputableValue<...>)` hook is available.
- Generated hook wiring must pass the memoized `ComputableValue` instance into the activation hook rather than the generated component instance.
- Runtime coverage should mirror the existing external-dependency memoize integration test but obtain the `ComputableValue` exclusively through `@OnActivate`.

## Command Surface

- Touch `processor/src/main/java/arez/processor/`.
- Add fixture input/expected source under `processor/src/test/fixtures/`.
- Update `processor/src/test/java/arez/processor/ArezProcessorTest.java`.
- Add integration coverage under `integration-tests/src/test/java/arez/integration/memoize/`.
- Update `CHANGELOG.md`.

## Quality Gates

- Targeted gate: `bundle exec buildr arez:processor:test`
- Targeted integration gate: `bundle exec buildr arez:integration-tests`
- Full gate: `bundle exec buildr test`

## Open Questions Register

- No open questions. The requested behavior maps directly to existing generator concepts and fixture conventions.
