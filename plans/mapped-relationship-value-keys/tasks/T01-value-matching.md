# T01 — Value matching and regressions

- Spec coverage: R1, R2, R3, AC1, AC2, AC3 (annotation and generation)

## Delivers

Annotation opt-in, generated configuration, consistent cache semantics, and real-component JVM/browser regressions.

## Acceptance criteria

- [ ] Default identity behavior remains; opt-in uses standard equality for lookup, occurrences, retention and reappearance.
- [ ] Tests prove original-source and local-state preservation, collision separation, null handling, disposed replacement and cleanup.
- [ ] Exact processor goldens and browser development/optimized tests pass.

## Validation

- Focused mapped-relationship integration tests, core annotation test, processor mapped-relationship goldens, and mapped-relationship browser targets in both modes.
- Format changed maintained Java and review generated output in both formats.

## Evidence

Pending.
