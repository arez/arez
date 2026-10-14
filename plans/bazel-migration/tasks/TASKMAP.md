# Task Map

- Spec: [SPEC.md](../SPEC.md)
- Status: closeout-pending
- Current frontier: None
- Planning reviewer: /root/planning_reviewer (2/3 rounds; passed, Findings: none)
- Plan checkpoint: human-review (approved)
- Checkpoint basis: the user said "Proceed" after the reviewed spec/task map and
  explicit plan-approval request on 2026-10-14. Planning review passed in two rounds.
- Implementation reviewer: /root/implementation_reviewer (5/5 rounds; passed, Findings: none)
- Active tree: plans/bazel-migration
- Plan commit: 3ce61e840aeb6d9fd6cc15dd193a201736a499ed
- Author preflight: passed — exact tree/link/acyclic graph and non-final-task
  R/AC coverage validator passed; task boundaries, staged failure accounting,
  release-source consumer sequencing, confirmed decisions and full-gate ownership
  inspected. Existing domain-documentation links also passed validation.

## Full-scope validation

- Gate: tools/check.sh, with fresh Bazel outputs/dependency context and no local
  module override, plus isolated unsigned-release/preview and documentation checks.
- Required contents: Buildifier; Palantir; full-graph Ahab; every retained JVM
  product and suite; strict positive goldens and plain-javac diagnostics; all
  eight browser suites in development and ADVANCED production; public packaged
  JVM/J2CL consumers; 32 release artifacts; site route/asset/link checks; nonempty
  JVM coverage reporting; legacy prerequisite/removal audit.
- Artifact reproducibility: independent-output-base hash comparisons, including
  binary, source, Javadoc and POM outputs; cached reruns alone are insufficient.
- Final evidence: [fresh complete gate](../T27-full-gate-evidence.json), [independent 33-file artifact hashes](../T27-reproducibility-evidence.json), [isolated artifact consumer](../T27-artifact-evidence.json), [inventory audit](../T27-inventory-audit.json), [final previews](../T27-preview-evidence.json).
- Result: tools/check.sh passed in a fresh checkout/output base with retired tools unavailable and no local override; 40 default tests, eight optimized browser tests, 31 coverage targets, 2,301 JVM TestNG invocations, 245 covered sources, complete artifacts/site/release stubs. All 33 archives/POMs and all 350 generated site files matched independent outputs.
- Author preflight: every R01–R17 / AC01–AC17 has completed delivery-owner evidence; all retained inventories, intentional cuts, generated strict checks, archived public consumers and canonical owners inspected. No required local gate failed/skipped. GitHub-hosted Linux CI and live publication were not run.
- Current milestone: all 27 tasks, the complete local gate, independent artifact/site comparisons, canonical knowledge promotion and implementation review passed. Same-reviewer round 5 returned Findings: none. Local PR content is prepared; explicit Git-push authority is the remaining publication dependency. Keep this exact tree until that dependency is resolved, then make the final deletion commit and publish a ready-for-human-review PR. Never merge or perform live release/deployment operations.

## Review-fix validation

- [Full gates](../review-full-gate-evidence.json): `tools/check.sh` passed in both the main and independent restricted-tool checkouts after correction, including a real separate public Bazel consumer. Both passed 40 default targets, eight production browser targets, 31 coverage targets and 2,303 JVM TestNG invocations.
- [Archive checks](../review-artifact-evidence.json) and [independent hashes](../review-reproducibility-evidence.json): all 33 artifacts match; all 350 site files also match. Site checks report 13,910 local links and zero broken links. Unsigned 32-artifact packaging and all release command stubs pass.
- [Round 1 disposition](../reviews/implementation-round-1.md): root configuration, Braincheck exports and original unmarked scopes corrected. Canonical spec/ADR updated; domain links and plan graph validation pass. Hosted Linux CI and live publication remain unexecuted.

- Round 2 correction evidence: [complete main gate](../review-round-2-full-gate-evidence.json), [archive checks](../review-round-2-artifact-evidence.json), [independent artifacts/site and targeted checks](../review-round-2-reproducibility-evidence.json), [finding disposition](../reviews/implementation-round-2.md). The main gate again passes 40/8/31 targets and 2,303 JVM invocations. Independent checks cover the corrected emission paths, packaged consumer assertions and unchanged 33-artifact/350-site-file boundary.

- Round 3 correction: [full main gate](../review-round-3-full-gate-evidence.json), [archive inspection](../review-round-3-artifact-evidence.json), [independent targeted checks and output comparison](../review-round-3-reproducibility-evidence.json), [review disposition](../reviews/implementation-round-3.md). Full gate passes 40/8/31 targets and 2,306 JVM invocations; 21 strict golden invocations/seven diagnostics and the packaged positive/negative consumer pass. No policy weakening or scope replanning was needed.

- Round 4 correction: [full main gate](../review-round-4-full-gate-evidence.json), [archive inspection](../review-round-4-artifact-evidence.json), [independent checks/output comparison](../review-round-4-reproducibility-evidence.json), [review disposition](../reviews/implementation-round-4.md). The full gate passes 40/8/31 targets and 2,306 JVM invocations; focused nullable/nonnull outside-transaction setters and packaged runtime use pass. All 33 artifacts and 350 site files match; no policy weakening or replanning was needed.

- Round 5: [Findings: none; implementation review passed](../reviews/implementation-round-5.md). The final domain-modeling sweep classified all reviewer-driven conclusions: original declaration/type-use preservation and representability are owned by the processor nullability specification; downstream root configuration and public strict dependencies by the publication ADR and development guidance; implementation mechanisms by code, and temporary proof by this tree. All changed canonical artifacts and local links validate; no qualifying conclusion remains unpromoted.

- Closeout readiness: all 27 tasks, required local validation, five-round implementation review, knowledge promotion and committed plan/implementation history are verified. Hosted Linux CI and live operations remain unexecuted. The local PR draft is `/tmp/arez-bazel-migration-pr.md`. Logs are retained under ignored `reports/verification/arez-bazel-migration/`; earlier execution paths in evidence identify where those logs originated. Plan deletion/its final commit and remote PR publication await explicit Git-push authority under the commit skill.

## Tasks

| ID | Task | Status | Blocked by |
| --- | --- | --- | --- |
| T01 | [Build and test core with Bazel](T01-bazel-core.md) | complete | None |
| T02 | [Run Arez processor regressions through Bazel](T02-processor-parity.md) | complete | T01 |
| T03 | [Compile generated integrations and documentation examples](T03-integration-doc-parity.md) | complete | T02 |
| T04 | [Build extras and run their JVM tests](T04-extras-parity.md) | complete | T03 |
| T05 | [Run the complete Persist JVM slice](T05-persist-parity.md) | complete | T04 |
| T06 | [Select and regenerate focused golden families](T06-focused-goldens.md) | complete | T05 |
| T07 | [Apply and check Palantir formatting](T07-java-format.md) | complete | T06 |
| T08 | [Migrate core API and runtime contracts to JSpecify](T08-core-jspecify.md) | complete | T07 |
| T09 | [Cut both processors to the pinned Proton and JSpecify API](T09-processor-jspecify-cut.md) | complete | T08 |
| T10 | [Migrate component, observable and lifecycle scenarios](T10-arez-basic-fixtures.md) | complete | T09 |
| T11 | [Migrate relationships, generics and remaining Arez scenarios](T11-arez-relationship-fixtures.md) | complete | T09 |
| T12 | [Restore the Persist slice after the nullability cut](T12-persist-jspecify.md) | complete | T10, T11 |
| T13 | [Restore extras, integrations and compiled documentation](T13-generated-consumers-jspecify.md) | complete | T12 |
| T14 | [Enforce strict checking on runtime products and tests](T14-runtime-strict-checks.md) | complete | T13 |
| T15 | [Enforce strict checking on processors and generated output](T15-processor-strict-checks.md) | complete | T14 |
| T16 | [Build deterministic binary and complete source archives](T16-release-binaries-sources.md) | complete | T15 |
| T17 | [Complete POM, Javadoc and artifact verification](T17-complete-release-artifacts.md) | complete | T16 |
| T18 | [Expose and test packaged JVM consumer targets](T18-packaged-jvm-consumers.md) | complete | T17 |
| T19 | [Compile public browser libraries from release sources](T19-packaged-j2cl-libraries.md) | complete | T18 |
| T20 | [Run and test the promise J2CL example](T20-promise-browser-example.md) | complete | T19 |
| T21 | [Run location, size and media-query examples](T21-dom-basic-examples.md) | complete | T20 |
| T22 | [Run visibility, geolocation, idle and network examples](T22-dom-controlled-examples.md) | complete | T20 |
| T23 | [Generate and preview the website with Java](T23-java-website.md) | complete | T17, T18 |
| T24 | [Replace manual release and deployment commands](T24-release-orchestration.md) | complete | T18, T23 |
| T25 | [Run the full check entrypoint in CI](T25-ci-ahab-coverage.md) | complete | T21, T22, T23, T24 |
| T26 | [Remove old infrastructure and document delivered contracts](T26-remove-legacy-document-contracts.md) | complete | T25 |
| T27 | [Verify the clean final migration and prepare implementation review](T27-final-verification.md) | complete | T26 |

## Acceptance ownership

T27 audits all requirements but does not replace the specific delivery owners.

| Requirement | Acceptance | Delivery owners |
| --- | --- | --- |
| R01 | AC01 | T01–T05, T13, T25, T27 |
| R02 | AC02 | T01–T05, T08–T15, T16, T18, T26 |
| R03 | AC03 | T01, T09, T16–T22, T25 |
| R04 | AC04 | T08–T13, T20–T22, T26 |
| R05 | AC05 | T07, T14–T15, T20–T22, T25 |
| R06 | AC06 | T01–T06, T09–T13, T15 |
| R07 | AC07 | T16–T17, T25 |
| R08 | AC08 | T18, T25 |
| R09 | AC09 | T19–T22 |
| R10 | AC10 | T20–T22 |
| R11 | AC11 | T17, T23, T25 |
| R12 | AC12 | T24, T26 |
| R13 | AC13 | T19–T20, T25 |
| R14 | AC14 | T25 |
| R15 | AC15 | T19, T23, T26 |
| R16 | AC16 | T06, T24, T26 |
| R17 | AC17 | All task commits; T26–T27; workflow review/closeout |

## Milestones and sequencing

1. T01–T05: full retained JVM build/test parity with existing semantics.
2. T06–T07: focused golden/update infrastructure and isolated formatting.
3. T08–T13: bounded JSpecify/Proton migration, restoring the full JVM gate at T13.
4. T14–T15: strict owned/generated checking, restoring the complete strict gate.
5. T16–T19: verified Maven artifacts and public JVM/J2CL consumers.
6. T20–T22: all eight local examples and the two-mode Chromium matrix.
7. T23–T24: Java website and release tooling.
8. T25–T27: CI/Ahab/coverage, removal/documentation/promotion, clean full-scope evidence.

The primary agent selects the lowest-numbered pending task whose blockers are
complete; only that task is in_progress. The frontier is every unblocked pending
task, including independent later tasks, not just the next number.

- T08–T13 is the explicit expand/migrate/contract exception for the wide refactor.
  The new Proton lacks old nullness helpers, so T09 changes both processor
  products together. T10/T11 own Arez case partitions, T12 owns Persist cases,
  and T13 owns remaining consumers and restores every JVM suite.
- T14–T15 is a shorter strict-enforcement transition. Every old/new case stays in
  its aggregate inventory while focused checks fix the active slice. No
  intermediate full pass is claimed until its complete gate actually passes.
- A task in either transition records expected pending-slice failures with
  target, cause and owner. Unexpected failures are investigated immediately.
  There is no second runtime semantics mode or legacy-annotation compatibility.
- Temporary old compiler/dependency scaffolding is build migration state only.
  T13 removes product nullability compatibility and T15 removes compiler
  opt-outs. Final deletion waits until replacement site/release/CI gates pass.
- Formatter changes are isolated; fixture inputs/goldens are not indiscriminately
  reformatted. A generated-output change is reviewed with its owning family.
- T10 and T11 partition original Arez cases by documented family membership and
  complement; their union must be checked against the T02/T06 inventory.
- Ahab is finalized against the whole delivered graph at T25, then rerun after
  deletion at T26/T27. Keep only justified browser exceptions, not silent findings.
- Source archives precede public consumers and browser examples so those paths
  validate the actual publication inputs.
- Local source targets remain the internal build API; public imports/plugins
  and J2CL labels explicitly consume assembled release archives.
- Do not perform actual Maven release/tag/GitHub release/site deployment or any
  downstream repository action. Use local copies/stubs for release validation.

## Intended validation interfaces

These labels/commands are contracts created by their owning tasks; their mention
in this plan is not a claim that they already exist.

- JVM products: //core, //processor, //extras/dom, //extras/promise,
  //extras/spytools, //extras/testng, //persist/core, //persist/processor,
  //doc-examples.
- JVM suites: //core:tests, //processor:all_tests, //integration-tests:tests,
  //extras/dom:tests, //extras/spytools:tests, //persist/core:tests,
  //persist/processor:all_tests, //persist/integration-tests:tests; //:jvm_tests.
- Public packaged JVM imports: //:core, //:processor, //:dom, //:promise,
  //:spytools, //:testng, //:persist_core, //:persist_processor.
- Packaged plugins: //:arez_processor_plugin, //:arez_persist_processor_plugin.
- Strict example/test Java: //extras/promise-example:java,
  //extras/dom-example:java; Java 17, JSpecify and the full owned checker policy.
- Public browser libraries: //:core-j2cl, //:dom-j2cl, //:promise-j2cl,
  //:spytools-j2cl, //:persist-core-j2cl.
- Examples: application/test/preview targets under extras/promise-example and
  extras/dom-example; //:browser_tests, default and -c opt.
- Complete tests: //:all_tests, plus an explicit -c opt //:browser_tests run.
- Release: //tools/release:binary_and_sources, :maven_artifacts,
  :site_javadoc; python3 tools/release/check_artifacts.py.
- Tooling: tools/java_format.sh, tools/update_goldens.sh,
  //tools/buildifier:buildifier_check, //tools/ahab:hermeticity.check,
  tools/site.sh build/check/serve, tools/check.sh.
- Documentation: the domain-modeling skill's scripts/validate_doc_links.py run
  against the repository root whenever canonical artifacts change.
- Plan: python3 plans/bazel-migration/validate_plan.py.

## Promoted knowledge

- Domain documentation existed at entry (docs/deferred); its later deletion
  does not waive the agreed classification/promotion sweep.
- T06 resolves the recorded golden-target question into durable development
  documentation before deleting the deferred issue.
- T26 classifies accepted nullability/publication/support invariants and
  architectural decisions. Promote only qualifying specifications/ADRs, update
  existing canonical owners instead of duplicating, validate links and record
  exact owner links here once written.
- Golden grouping/update ownership: [development documentation](../../../docs/development.md); deferred issue resolved/deleted in T06, link validation passed.
- Promotion status: complete at T26; [candidate classification](../knowledge-classification.md) and specification-only audit passed.
- Canonical contracts: [processor nullability](../../../docs/specs/processor-nullability.md).
- Canonical decisions: [Bazel build](../../../docs/adr/0001-bazel-build.md), [public archive boundary](../../../docs/adr/0002-publication-artifact-boundary.md).
- Existing operational owners: [development](../../../docs/development.md), [setup](../../../docs/project_setup.md), [release](../../../docs/releasing.md).
- Concrete existing defects: [bug backlog](../../../TODO.md#known-bugs); inverse construction limitation: [reference documentation](../../../docs/at_reference.md#inverse).
- No new glossary/use case/deferred issue qualified. Domain link validator passed against the repository root; final T27 sweep verifies these owners before review.

## Review and closeout routing

- Round 1: [two blocking findings, corrected](../reviews/planning-round-1.md).
- Round 2: [Findings: none; planning review passed](../reviews/planning-round-2.md).

After user plan approval, validate this exact tree and commit it before code
changes. Follow the commit skill, normal hooks and explicit staging boundaries.
Implement tasks sequentially with their evidence/map-state commits.

After T27, perform the domain knowledge sweep and use one fresh read-only
implementation reviewer for up to five rounds. Preserve the phase's reviewer
identity; materially changed scope returns to this same planning reviewer and
the human plan checkpoint.

Closeout follows the invoked workflow: all tasks/full validation/promotions and
implementation review must pass, history must contain the plan/implementation
updates, and only this exact temporary tree is removed in the closeout commit.
A ready-for-human-review pull request is the workflow's final remote deliverable;
its publication is subject to explicit human Git-push authority. Complete all
local reviewable work before requesting any missing authority and keep the tree
until the closeout gate can be satisfied. Never merge the pull request.
