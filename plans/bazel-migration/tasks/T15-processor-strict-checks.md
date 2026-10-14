# T15 — Enforce strict checking on processors and generated output

- Status: complete
- Blocked by: T14
- Spec coverage: R01, R02, R05, R06, AC01, AC02, AC05, AC06

## Delivers

Every owned compilation and valid golden output passes strict checking; diagnostic inputs retain plain-javac isolation.

## Acceptance criteria

- [x] Apply the agreed strict policy to processors, test support, integrations and compiled documentation. Activate an injected Error Prone/NullAway compiler for positive golden families.
- [x] Check generated types retain nested/inherited JSpecify contracts; fix the generator rather than blanket-excluding generated code.
- [x] Keep deliberate negative fixtures/forked-javac checks on plain javac; scope any intentional fixture exception to that case and explain it.
- [x] Remove temporary compiler-policy opt-outs/definitions from the migration. All maintained source wrappers enforce the final policy.
- [x] Regenerate changed output families, preserve exact outputs/behavior and complete all tracked strict-enforcement failures.

## Validation

- Build every retained JVM product/doc target and bazel test //:jvm_tests --test_output=errors — full strict JVM milestone.
- Run every positive family with the checking compiler and every diagnostic/warning/forked target with its documented compiler contract.
- Inspect representative compile actions and add a controlled checker violation to a disposable fixture to prove strict generated-body enforcement, then remove it.
- tools/java_format.sh check and focused idempotent golden updates — verify clean final generated content.

## Evidence

- Removed every temporary compiler-policy opt-out. Both processors, their QA support, integrations, documentation and retained runtime test sources compile under the central Java 17 / Werror / Sting 78-check plus defaults / JSpecify NullAway policy. [Policy evidence](../T15-policy-evidence.json) records the exact reference match and narrow exceptions.
- Positive fixtures use plain javac to generate outputs and compiled declarations, then an injected Error Prone/NullAway compiler checks every generated Java body in both modes. The second compilation has an empty sourcepath and includes multi-stage base outputs; deliberate diagnostics, warnings and forked javac retain their original plain compiler. No generated path is excluded.
- Fixed generated kernel lifetime access, erased property-mutator boundaries, generic observable/memoize type arguments, nested return contracts, local/cast annotation placement, cached collection flow, equality generation and captured dependency references. Removed unused generated fields/methods. Primitive Persist IDs no longer receive a meaningless null check.
- Preserved ONE inverse reads before binding: a new assertion broke four existing observer scenarios, so it was removed from those getters and replaced by a method-scoped NullAway suppression with an explanation. All 191 integration cases now pass. Default array identity, BigDecimal scale, concrete collection/Date equality and context-thread synthetic counters retain their prior behavior through narrowly explained checker suppressions.
- Corrected permissive fixture/support annotations where actual factories and final collections cannot return/become null. Deliberate method-local type parameters and the legacy-annotation contradiction have explicit fixture-method suppressions, carried to their generated overrides. Original provider/scenario membership remains intact.
- `bazel build //core //processor //extras/dom //extras/promise //extras/spytools //extras/testng //persist/core //persist/processor //doc-examples` passed. `bazel test //:jvm_tests --keep_going --test_output=errors` passed all 31 targets: [2,299 TestNG invocations, no failures or skips](../T15-jvm-results.json). This closes every tracked T14–T15 enforcement failure.
- [Controlled probe](../T15-checker-probe.json): returning null from the generated SubpackageModel kernel accessor failed with ReturnMissingNullable and NullAway. The mutation was removed; the family and aggregate gate subsequently passed.
- Regenerated affected Arez and Persist families without deleting original cases or expected paths. [Focused update idempotence](../T15-golden-idempotence.json) compares all 1,221 golden file hashes before/after rerunning Arez JSpecify and Persist model updates. Both exact-output modes remain asserted.
- [Inventory](../T15-test-inventory.json) verifies unchanged method membership in 270 retained JVM test sources and unchanged contents of all 37 core/integration runtime JSON fixtures. DOM tests now use a nonnull fake event rather than an obsolete native-event null placeholder; their assertions and case membership are retained.
- Palantir, Buildifier, whitespace and plan validation passed. The pre-existing Persist Object restoration issue recorded in T12 remains unchanged and owned by the T26 knowledge sweep.
