# T02 — Add mapped relationship opt-in

- Status: complete
- Blocked by: T01
- Spec coverage: R1, R2, R3, R4, R5, R6

## Delivers

MappedRelationship supports component-like declared targets with explicit
runtime validation under the same policy. Preserve all existing mapping
contracts and demonstrate runtime liveness/failure through generated components.

## Acceptance and validation

- New annotation option defaults to false; processor matrix includes redundant
  opt-in warnings/suppression and invalid declarations.
- Strict generated compilation covers declared abstract/interface callbacks and
  inherited compiled declarations.
- Real-component tests cover hidden and retained targets, invalid factory values
  including hidden results, deactivation/disposal, and retry/cleanup behavior.
- Packaged J2CL consumers exercise component-like mappings in both build modes.
- API docs, guides, changelog and canonical shared/mapped specs match behavior.
- Run focused processor/core/integration checks, domain document link validation,
  then tools/check.sh before committing final implementation and entering review.

## Evidence

- Shared processor matrix: 142 cases passed, including warning suppression, known/unknown target types, simple-name framework markers, compiled generic bases and both formatting modes.
- Core and integration suites passed, including the same packaged real-component scenarios with API invariants enabled and disabled.
- Packaged mapped browser tests passed in development (11.3s) and optimized production (8.3s).
- Domain documentation classified/promoted to docs/specs/component-target-validation.md and linked from the mapped spec; document-link validation passed.
- A direct compilation probe exposed accidental acceptance of unresolved generic ComponentDependency getters. Restored its existing declared-return-type restriction; a regression test rejects the declaration before any source is emitted. Concrete inherited generic bindings remain supported.
- Full gate passed with `USE_BAZEL_VERSION=9.3.1 tools/check.sh` (`All Arez checks passed.`). First unpinned attempt failed only on Bazelisk latest-version DNS lookup in isolated consumers; pinning the repository's required tool version cleared those checks. A superseded coverage run was intentionally stopped for the generic-getter regression.
- Final full gate: 46 development targets, 10 optimized browser targets and 35 JVM coverage targets passed. Coverage floors, format/build hermeticity, packaged consumers, API docs/site and unsigned release bundle checks passed.
