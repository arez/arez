# Task Map

- Spec: [SPEC.md](../SPEC.md)
- Status: `implementing`
- Current frontier: `T02`
- Planning reviewer: `/root/planning_review` (`1/3` rounds; final confirmation `unused`; Findings: none)
- Plan checkpoint: `automatic` — confirmed scope and execution authority, no requested plan pause, passing independent planning review.
- Implementation reviewer: `pending` (`0/5` rounds; final confirmation `unused`)
- Authority: confirmed interview contract and user request to implement with structured-delivery-workflow and create a PR. No additional human plan checkpoint.
- Active tree: `plans/mapped-relationships/`; new invocation, not a resumed tree.

## Full-scope validation

- Gate: `tools/check.sh`, plus domain-artifact link validation.
- Evidence: pending.

## Tasks

| ID | Task | Status | Blocked by |
| --- | --- | --- | --- |
| T01 | [Generate identity-preserving mapped relationships](T01-basic-mapping.md) | complete | None |
| T02 | [Add reactive presentation and absent-target retention](T02-presentation-retention.md) | pending | T01 |
| T03 | [Document, exercise browsers and validate delivery](T03-documentation-validation.md) | pending | T02 |

## Sequencing notes

- Each implementation task includes the tests and public Javadoc for its delivered behavior; broader validation does not postpone focused verification.
- Keep ordinary memoization behavior/goldens unchanged unless a necessary shared-path correction is demonstrated and reviewed.
- Run an independent full implementation review after all slices and full-scope validation. Closeout promotes durable knowledge, removes this exact tree, verifies the final diff and creates the review PR; never merge it.

## Review history

- Planning round 1: Findings: none. Complete plan/graph, accepted corrections, callback/nullability, memoization/equality/cleanup, aggregate/publication wiring and Rose evidence checked. Implementation/full gate remain unverified. Budget 1/3 used; final confirmation unused.
- Implementation review: pending; budget 0/5 used, final confirmation unused.

## Promoted knowledge

- Pending: mapped relationship contract, API Javadoc and user guide owned by T03.

## Environment evidence

- Baseline core JVM tests passed before implementation.
- System Bazel is 9.3.0; repository requires 9.3.1. Task-local `/tmp/arez-mapped-tools.7g1LrY/bazel` is upstream 9.3.1, verified against its published SHA-256. Prefix its directory on PATH for validation; do not change the shared installation or project pin.
