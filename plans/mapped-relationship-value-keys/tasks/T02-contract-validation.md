# T02 — Contract and complete validation

- Spec coverage: R4, AC3 (documentation), AC4

## Delivers

Durable documentation and complete validation before independent implementation review and PR closeout.

## Acceptance criteria

- [ ] API, guide, specification, identity rationale and changelog agree on both policies and key constraints.
- [ ] Domain artifact links validate and specification-only diff passes the replacement test.
- [ ] Full gate passes and implementation is ready for independent review.
- [ ] At closeout, review passes, temporary plan is removed and a PR is pushed for review.

## Validation

- Domain-modeling link validator; `git diff --check`; `tools/check.sh`.
- Independent review and closeout evidence recorded in TASKMAP before deleting this tree.

## Evidence

Pending.
