# T27 — Verify the clean final migration and prepare implementation review

- Status: complete
- Blocked by: T26
- Spec coverage: R01, R02, R03, R04, R05, R06, R07, R08, R09, R10, R11, R12, R13, R14, R15, R16, R17, AC01, AC02, AC03, AC04, AC05, AC06, AC07, AC08, AC09, AC10, AC11, AC12, AC13, AC14, AC15, AC16, AC17

## Delivers

Task evidence and a clean final full-scope gate establish that the migration is ready for the workflow's implementation reviewer.

## Acceptance criteria

- [x] Verify all task criteria, retained inventories, intentional cuts, public consumer/archive parity and promoted knowledge against the confirmed spec.
- [x] Run the complete check entrypoint without a local module override in a clean dependency/output environment; make host Ruby/Buildr/Node/Yarn unavailable for the prerequisite check.
- [x] Ensure coverage reports are nonempty, all eight browser apps execute in both modes, independent artifact rebuild comparisons passed and no scratch/debug files remain.
- [x] Record exact commands/results, residual platform/tool limits and full-gate evidence in TASKMAP.md; no required failed/skipped check can be reported as passing.
- [x] Commit final implementation/task/map state, then route to the fresh read-only implementation reviewer. Closeout is a workflow stage, not part of this task.

## Validation

- tools/check.sh with fresh Bazel output/cache context and no local override — strongest practical final gate.
- Repeat unsigned release preview validation and local site/example preview smoke checks using final files.
- git diff --check, final diff/status inspection, task-graph/link validator and domain documentation validator.
- Author preflight maps each R/AC to recorded evidence before implementation review; unresolved evidence blocks completion.

## Evidence

- Fresh local clone of 44febb4cc with a new empty Bazel output base and no local module override ran the entire tools/check.sh successfully. Wrapper startup flags disabled system/home rc; a PATH of allowlisted Unix tools plus Bazel/Python/Java excluded Ruby/Buildr/Bundler/Node/Yarn/npm and GPG. Environment/root/commit are captured in T27-clean-environment.json; complete results in T27-full-gate-evidence.json.
- Fresh executions: all 40 default test targets, all eight ADVANCED browser targets (22 cases per mode), every strict Java 17 example/test target, packaged JVM/J2CL consumers, all 31 coverage targets and 2,301 JVM TestNG invocations. Coverage has 245 source files and 11,315 exercised lines, with no percentage gates.
- 32 publication files plus aggregate API archive match the primary output base byte-for-byte; T27-reproducibility-evidence.json records all 33 SHA-256 values. The isolated Java 21 consumer/processor checker passed again, including compiled inherited JSpecify contracts. Entire generated site matches across the two checkouts (350 files).
- Fresh unsigned preview has 32 artifacts / 96 entries with coordinates/checksums verified; release preparation/sign/upload/GitHub/site plumbing passed only through disposable copies/local stubs. No live signing/tag/upload/release/deploy occurred.
- Final site/server previews verified setup, SVGs, generated snippets and aggregate API without console errors. Promise fulfilled its local fetch (200 OK); ADVANCED location route button updated both displayed values to /event. All temporary preview tabs/servers closed. Final tracked audit has no legacy config, downstream module or GWT descriptor/bootstrap; required native companions/markers remain.
- Inventory audit retains 270 recorded JVM test source files, all 2,191 recorded method/constructor declarations, all 1,025 Arez processor fixture classname entries and all 101 original Persist fixture input/golden paths. T01–T26 criteria/evidence and all R/AC owners were checked before final completion.
- Plan/link/canonical documentation validators and git diff --check pass. Qualified knowledge is promoted and linked in TASKMAP; no new deferral, debug change or tracked scratch file remains. Temporary independent verification caches are task evidence and will be removed after review.
- Practical platform limit: full gate executed on macOS arm64; the GitHub-hosted Linux job has not run from this unpushed branch. Live publication remains deliberately outside authority. Implementation review is the next required workflow gate.

## Review-fix final verification

- After round 1 corrections, `tools/check.sh` passed again in the main checkout and the independent checkout/output base with retired tools unavailable and system/home rc ignored. The updated gate includes a separate real Bazel consumer of all six runtime imports and both packaged plugins.
- Both runs pass 40 default tests, eight production browser targets, 31 coverage targets and 2,303 JVM TestNG invocations. Coverage includes 245 sources, 12,358 lines/11,367 hits and 6,091 branches/4,938 hits. No percentage gate was added.
- Archive checker plus direct hashes confirm all 33 final artifacts match. All 350 generated site files match; 13,910 links pass with zero broken. Unsigned 32-artifact bundle verification and release preparation/sign/upload/GitHub/site stubs pass. [Full gate evidence](../review-full-gate-evidence.json), [archive evidence](../review-artifact-evidence.json), [independent hashes](../review-reproducibility-evidence.json).
- Canonical spec/ADR and affected T09/T18 evidence are updated. Plan/domain links and staged whitespace pass. Same-reviewer confirmation remains pending; no publication occurred.

## Round 2 final verification

- The complete main `tools/check.sh` gate passed after the constructor/ref materialization correction: 40 default targets, eight production browser targets, 31 coverage targets and 2,303 JVM invocations, including emitted-contract assertions in the separate public packaged consumer. Independent output-base rebuild, strict JSpecify goldens, packaged consumer and site build/check passed with retired tools unavailable. All 33 final artifacts and 350 site files match.
- [Latest full gate](../review-round-2-full-gate-evidence.json), [latest archive inspection](../review-round-2-artifact-evidence.json), [independent boundary evidence](../review-round-2-reproducibility-evidence.json). These supersede the prior artifact hashes for the changed processor implementation. Canonical spec/Unreleased notes, plan and domain links validate. Same-reviewer confirmation is next.

## Round 3 final verification

- The complete main gate passed after the precise unrepresentable-parametric diagnostic and preserving setter-local correction: 40 default targets, eight production browser targets, 31 coverage targets and 2,306 JVM invocations. The separate public packaged consumer passes positive runtime/signature checks and asserts the new diagnostic with actionable guidance.
- Independent restricted-tool artifact rebuild, 21 strict golden invocations/seven diagnostics, packaged positive/negative consumer and site build/check pass. All 33 archives/POMs and all 350 site files match. [Full gate](../review-round-3-full-gate-evidence.json), [archive checks](../review-round-3-artifact-evidence.json), [independent outputs](../review-round-3-reproducibility-evidence.json). These are the latest processor artifact hashes. Canonical requirements/Unreleased notes and plan/domain links validate. Same-reviewer confirmation is next.

## Round 4 final verification

- After preserving the private outside-transaction setter parameter, the complete main gate passed: 40 default targets, eight production browser targets, 31 coverage targets and 2,306 JVM invocations. Paired goldens cover nested nullable/nonnull components in this setter mode, and the separate packaged consumer compiles and runs a nullable-element outside-transaction write.
- Independent restricted-tool artifacts, strict contracts, packaged positive/negative consumer and site build/check pass. All 33 artifacts and 350 site files match. [Full gate](../review-round-4-full-gate-evidence.json), [archive inspection](../review-round-4-artifact-evidence.json), [independent comparison](../review-round-4-reproducibility-evidence.json), [review disposition](../reviews/implementation-round-4.md). These supersede prior processor artifact hashes. Canonical requirements and Unreleased notes are updated; plan/domain links and whitespace pass. Same-reviewer round 5 is next.
