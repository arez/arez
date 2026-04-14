# Implementation Plan

## Phase Sequence

1. Add structured-delivery artifacts for the memoize/on-activate fix.
2. Relax memoize validation so `AREZ_OR_EXTERNAL` accepts an `@OnActivate` hook that already receives the `ComputableValue`.
3. Align generated activation hook wiring so parameterized `@OnActivate` hooks receive the memoize field through the wrapper path.
4. Add a processor regression fixture.
5. Add an integration scenario covering runtime use of the `ComputableValue` obtained from `@OnActivate`.
6. Run targeted processor and integration verification.

## Delivery Approach

- Keep the behavior change narrow and local to memoize lifecycle handling.
- Reuse the existing hook wrapper path instead of introducing a new special-case code path.
- Cover the accepted pattern with both a compile fixture and a runtime integration test.

## High-Risk Areas

- Risk 1: validation may accidentally loosen the rule for unrelated hook signatures.
  - Mitigation: only waive the `@ComputableValueRef` requirement when the `@OnActivate` parameter raw type is `arez.ComputableValue`.
- Risk 2: generated hook wiring may still call the wrong overload path.
  - Mitigation: route parameterized activation hooks through the wrapper helper that already passes the memoize field.
- Risk 3: the runtime test may accidentally prove only activation, not external invalidation.
  - Mitigation: store the hook-delivered `ComputableValue` on the component and call `reportPossiblyChanged()` through that stored handle after mutating external state.

## Required Full Gates

- `bundle exec buildr arez:processor:test`
- `bundle exec buildr arez:integration-tests`
- `bundle exec buildr test`

## Decision Log

- Processor fixtures are the right regression surface because the bug is caught during annotation processing.
- The generator fix should reuse the existing activation wrapper helper so the hook always receives the memoized `ComputableValue`.
- The integration scenario should prove the hook-delivered `ComputableValue` is the one needed to signal external dependency changes at runtime.

## Completion Criteria

- `@Memoize( depType = DepType.AREZ_OR_EXTERNAL )` compiles without `@ComputableValueRef` when `@OnActivate` accepts the associated `ComputableValue`.
- The previous missing-ref failure still applies when no hook or ref exposes the computable value.
- The targeted `bundle exec buildr arez:processor:test` gate passes.
- The targeted `bundle exec buildr arez:integration-tests` gate passes.
- The broader `bundle exec buildr test` gate passes or any unrelated failure is captured in evidence.
