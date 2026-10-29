# Task Map

- Spec: [SPEC.md](../SPEC.md)
- Status: `closeout`
- Current frontier: remove the exact active tree and publish the review PR
- Planning reviewer: `/root/planning_review` (`1/3` rounds; final confirmation `unused`; Findings: none)
- Plan checkpoint: `automatic` — confirmed scope and execution authority, no requested plan pause, passing independent planning review.
- Implementation reviewer: `/root/mapped_implementation_review` (`3/5` rounds; final confirmation `unused`; Findings: none)
- Authority: confirmed interview contract and user request to implement with structured-delivery-workflow and create a PR. No additional human plan checkpoint.
- Active tree: `plans/mapped-relationships/`; new invocation, not a resumed tree.

## Full-scope validation

- Gate: `tools/check.sh`, plus domain-artifact link validation.
- Evidence: T03 records the passing complete gate, browser matrix, packaged consumers, unchanged coverage floors and link validation; all task-local checks passed.

## Tasks

| ID | Task | Status | Blocked by |
| --- | --- | --- | --- |
| T01 | [Generate identity-preserving mapped relationships](T01-basic-mapping.md) | complete | None |
| T02 | [Add reactive presentation and absent-target retention](T02-presentation-retention.md) | complete | T01 |
| T03 | [Document, exercise browsers and validate delivery](T03-documentation-validation.md) | complete | T02 |

## Sequencing notes

- Each implementation task includes the tests and public Javadoc for its delivered behavior; broader validation does not postpone focused verification.
- Keep ordinary memoization behavior/goldens unchanged unless a necessary shared-path correction is demonstrated and reviewed.
- Callback generation must preserve the selected parameter signature: a plain method reference can select a more-specific overload with an incompatible return type. T02 adds regression coverage and fixes factory and predicate calls together under R4.
- Run an independent full implementation review after all slices and full-scope validation. Closeout promotes durable knowledge, removes this exact tree, verifies the final diff and creates the review PR; never merge it.
- Clarified T03's review acceptance item to supply the committed inputs; the independent implementation review remains the mandatory post-task gate rather than a circular prerequisite for completing T03.

## Review history

- Planning round 1: Findings: none. Complete plan/graph, accepted corrections, callback/nullability, memoization/equality/cleanup, aggregate/publication wiring and Rose evidence checked. Implementation/full gate remain unverified. Budget 1/3 used; final confirmation unused.
- Implementation round 1: two verified P2 findings. Nested JSpecify compatibility is omitted from callback selection, and original generic container returns are cast unsafely/classified before substitution. Fix both under existing R4/AC2, add conventional/explicit and source/compiled regressions, and revalidate affected generation/runtime plus the full gate. Remaining budget 4/5, final confirmation unused. Complete delivered scope reviewed; reviewer left the repository unchanged.

- Round 1 dispositions: both findings fixed under R4/AC2. T03 records nested-contract/variance/explicit binding regressions, source/compiled generic container extraction and persistent-target cleanup tests in both wrapping modes. Latest complete gate passed; no new scope or significant decision. Ready for round 2; 1/5 consumed, final confirmation unused.
- Implementation round 2: original findings confirmed fixed. One verified P2 introduced by the new compatibility check: it rejects safe NONNULL-to-PARAMETRIC callback input flow after nominal assignability. Permit that subtype relation while retaining invariant bidirectional checks; add direct and wildcard regressions plus nullable-to-parametric rejection. Remaining regular budget 3/5; final confirmation unused.

- Round 2 disposition: safe NONNULL-to-PARAMETRIC flow fixed; direct/wildcard positive and nullable/invariant negative regressions pass. Latest complete gate passed with unchanged floors. Ready for round 3; 2/5 consumed, final confirmation unused.

- Implementation round 3: Findings: none. Original probe, direct/wildcard generic strict compilation, invariant/nullable rejections, both golden modes, wiring and exact-state full-gate evidence checked. All three P2 findings resolved. 3/5 rounds consumed, final confirmation unused; no remaining implementation findings.

## Promoted knowledge

- [Mapped relationship contract](../../../docs/specs/mapped-relationships.md), [identity rationale](../../../docs/adr/0003-mapped-relationship-identity.md), [user guide](../../../docs/at_mapped_relationship.md) and public annotation Javadoc. Classification, replacement test and specification-only audit completed; links validated.

## Environment evidence

- Baseline core JVM tests passed before implementation.
- System Bazel is 9.3.0; repository requires 9.3.1. Task-local `/tmp/arez-mapped-tools.7g1LrY/bazel` is upstream 9.3.1, verified against its published SHA-256. Prefix its directory on PATH for validation; do not change the shared installation or project pin.

## Closeout gate

- All tasks complete; focused checks and final full gate passed. Plan and implementation snapshots are committed. Independent implementation review passed after three rounds.
- Final knowledge sweep reuses the prior classifications. Review fixes implement the existing callback/nullability and lifecycle requirements; no new durable decision, term or deferred question. Canonical artifact links validate and no external inbound reference requires this temporary tree.
- Remove only `plans/mapped-relationships/` after committing this review record. Publish a ready-for-review PR and leave merge to the user.
