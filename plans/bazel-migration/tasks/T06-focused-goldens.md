# T06 — Select and regenerate focused golden families

- Status: pending
- Blocked by: T05
- Spec coverage: R06, R16, AC06, AC16

## Delivers

Arez and Persist golden families have focused Bazel execution and idempotent update commands while retaining all existing scenarios.

## Acceptance criteria

- [ ] Partition generated-output providers by feature families, compile shared support once and preserve a complete mapping of every original row/special method to a target. The current successfulCompiles provider is shared across families: split it into selectable feature methods/providers rather than assuming TestNG method filters can select individual provider rows. Reuse the existing assertions; do not add a generic filtering framework.
- [ ] Keep diagnostics/warnings/forked-javac in their execution contracts; retain message-fragment expectations in Java unless a narrower representation is required by the existing runner.
- [ ] Add a focused tools/update_goldens.sh workflow for both processors that copies both output trees from runfiles, checks exact files and reruns the selected family.
- [ ] Own the equivalent focused regeneration/declared inputs for core invariant JSON and integration spy-event fixtures.
- [ ] Transfer the golden-grouping decision and rationale to a durable development-documentation section, remove/update backlinks, delete docs/deferred/processor-test-bazel-shape.md and validate the changed documentation.
- [ ] Do not add a generic per-fixture runner or full-suite update shortcut that hides changed families.

## Validation

- bazel test //processor:all_tests //persist/processor:all_tests --test_output=errors — all partitioned cases still execute.
- Regenerate one representative Arez family and one Persist family twice; both trees must be unchanged on the second run and focused tests must pass.
- Compare case manifests before/after grouping, including warning and multi-stage cases — proves none disappeared.
- Run the domain-modeling link validator after resolving the existing deferred issue.

## Evidence

- pending
