# T06 — Select and regenerate focused golden families

- Status: complete
- Blocked by: T05
- Spec coverage: R06, R16, AC06, AC16

## Delivers

Arez and Persist golden families have focused Bazel execution and idempotent update commands while retaining all existing scenarios.

## Acceptance criteria

- [x] Partition generated-output providers by feature families, compile shared support once and preserve a complete mapping of every original row/special method to a target. The current successfulCompiles provider is shared across families: split it into selectable feature methods/providers rather than assuming TestNG method filters can select individual provider rows. Reuse the existing assertions; do not add a generic filtering framework.
- [x] Keep diagnostics/warnings/forked-javac in their execution contracts; retain message-fragment expectations in Java unless a narrower representation is required by the existing runner.
- [x] Add a focused tools/update_goldens.sh workflow for both processors that copies both output trees from runfiles, checks exact files and reruns the selected family.
- [x] Own the equivalent focused regeneration/declared inputs for core invariant JSON and integration spy-event fixtures.
- [x] Transfer the golden-grouping decision and rationale to a durable development-documentation section, remove/update backlinks, delete docs/deferred/processor-test-bazel-shape.md and validate the changed documentation.
- [x] Do not add a generic per-fixture runner or full-suite update shortcut that hides changed families.

## Validation

- bazel test //processor:all_tests //persist/processor:all_tests --test_output=errors — all partitioned cases still execute.
- Regenerate one representative Arez family and one Persist family twice; both trees must be unchanged on the second run and focused tests must pass.
- Compare case manifests before/after grouping, including warning and multi-stage cases — proves none disappeared.
- Run the domain-modeling link validator after resolving the existing deferred issue.

## Evidence

- Full processor aggregates passed: all 463 Arez golden cases across nine provider families plus 69 special scenarios; all 23 Persist cases across model/types. Both golden modes remain checked. Diagnostics retain 677 Arez and 31 Persist cases plus both forked-javac checks.
- [Case map](../golden-case-map.json) maps every original provider row/special method to exactly one target. Counter comparisons against pre-grouping providers and exact comparisons of special-method/assertion bodies passed. Every family executed its expected count without skips.
- `tools/update_goldens.sh arez action` and `... persist types` each ran twice; neither pass changed golden content. Updates use disposable copies and rerun the selected family.
- `tools/update_runtime_fixtures.sh core-invariants` passed all 843 core cases; the NativeComponents.inspectComponentViaSpy selection passed exactly one scenario. Both JSON outputs remained unchanged and subsequent checks passed.
- Bash syntax, Buildifier and domain-modeling documentation link checks passed. [Development documentation](../../../docs/development.md) owns grouping rationale and mutable commands; the resolved deferred document and live backlinks are removed.
- Group membership remains in Java providers/BUILD declarations. No per-fixture runner or diagnostic-expectation format was added.
