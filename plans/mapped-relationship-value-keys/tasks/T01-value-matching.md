# T01 — Value matching and regressions

- Spec coverage: R1, R2, R3, AC1, AC2, AC3 (annotation and generation)

## Delivers

Annotation opt-in, generated configuration, consistent cache semantics, and real-component JVM/browser regressions.

## Acceptance criteria

- [x] Default identity behavior remains; opt-in uses standard equality for lookup, occurrences, retention and reappearance.
- [x] Tests prove original-source and local-state preservation, collision separation, null handling, disposed replacement and cleanup.
- [x] Exact processor goldens and browser development/optimized tests pass.

## Validation

- Focused mapped-relationship integration tests, core annotation test, processor mapped-relationship goldens, and mapped-relationship browser targets in both modes.
- Format changed maintained Java and review generated output in both formats.

## Evidence

- New real-component JVM regression passed: `bazel test //integration-tests:tests --test_output=errors --test_arg=-methods --test_arg=arez.integration.mapped_relationship.MappedRelationshipIntegrationTest.preservesValueKeyTargetsThroughPackagedComponents` (1 test, no skips).
- `tools/update_goldens.sh arez mapped_relationship` passed (16 tests). Reviewed all 28 constructor changes across 22 golden files; no other generated differences. Explicit and inherited/compiled-base opt-in are covered; conventional defaults emit false.
- Annotation default test passed: `bazel test //core/src/test/java/arez:tests --test_output=errors --test_arg=-methods --test_arg=arez.MappedRelationshipTest.exposesEveryMemoizeOptionWithTheSameDeclaredDefault`.
- `bazel test //core/src/test/js:browser_test --test_output=errors` and its `-c opt` equivalent passed (3/3 browser scenarios in each mode). Initial development runner startup timed out at WSL health check; unchanged retry passed.
- `tools/java_format.sh write`, `tools/java_format.sh check` and `git diff --check` passed.
- Fixture corrections during verification: use the declared base type to access nested private test state; batch owner/observer disposal in one action to prevent an observer reading a disposing owner, matching existing lifecycle tests.
- Simplify review: keep one map-selection helper and the existing encounter-order list/result comparator; no additional abstraction or compatibility constructor.
