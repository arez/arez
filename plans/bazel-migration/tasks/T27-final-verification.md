# T27 — Verify the clean final migration and prepare implementation review

- Status: pending
- Blocked by: T26
- Spec coverage: R01, R02, R03, R04, R05, R06, R07, R08, R09, R10, R11, R12, R13, R14, R15, R16, R17, AC01, AC02, AC03, AC04, AC05, AC06, AC07, AC08, AC09, AC10, AC11, AC12, AC13, AC14, AC15, AC16, AC17

## Delivers

Task evidence and a clean final full-scope gate establish that the migration is ready for the workflow's implementation reviewer.

## Acceptance criteria

- [ ] Verify all task criteria, retained inventories, intentional cuts, public consumer/archive parity and promoted knowledge against the confirmed spec.
- [ ] Run the complete check entrypoint without a local module override in a clean dependency/output environment; make host Ruby/Buildr/Node/Yarn unavailable for the prerequisite check.
- [ ] Ensure coverage reports are nonempty, all eight browser apps execute in both modes, independent artifact rebuild comparisons passed and no scratch/debug files remain.
- [ ] Record exact commands/results, residual platform/tool limits and full-gate evidence in TASKMAP.md; no required failed/skipped check can be reported as passing.
- [ ] Commit final implementation/task/map state, then route to the fresh read-only implementation reviewer. Closeout is a workflow stage, not part of this task.

## Validation

- tools/check.sh with fresh Bazel output/cache context and no local override — strongest practical final gate.
- Repeat unsigned release preview validation and local site/example preview smoke checks using final files.
- git diff --check, final diff/status inspection, task-graph/link validator and domain documentation validator.
- Author preflight maps each R/AC to recorded evidence before implementation review; unresolved evidence blocks completion.

## Evidence

- pending
