# T25 — Run the full check entrypoint in CI

- Status: complete
- Blocked by: T21, T22, T23, T24
- Spec coverage: R01, R03, R05, R06, R07, R08, R09, R10, R11, R13, R14, AC01, AC03, AC05, AC06, AC07, AC08, AC09, AC10, AC11, AC13, AC14

## Delivers

One local/CI command verifies the retained graph, browser matrix, release consumers, site and reviewed hermeticity baseline.

## Acceptance criteria

- [x] Create tools/check.sh and CI using Java/Bazel and documented Python/Bash tools, removing Ruby/Node setup and Buildr commands from active CI.
- [x] Run Buildifier, formatter, Ahab, complete JVM builds/tests, strict Java 17 example/browser-test builds (//extras/promise-example:java and //extras/dom-example:java), packaged consumer/source checks, all browser tests in both modes and site checks. Include every owned example/test source in the strict checker gate.
- [x] Port Ahab check/update/explain targets into tools/ahab; inspect every finding, declare custom tool reproducibility and retain only justified target-scoped browser exceptions.
- [x] Generate nonempty coverage reports for retained JVM suites and publish/retain them as CI artifacts without new percentage thresholds.
- [x] Expose //:all_tests containing retained JVM tests and browser tests; also maintain explicit suite aliases so the optimized browser invocation cannot be omitted.
- [x] Record actual tool versions/pins and CI command paths; do not accept unexplained baseline additions as fixes.

## Validation

- tools/check.sh — mandatory complete CI-equivalent gate.
- bazel run //tools/ahab:hermeticity.check and :hermeticity.explain — review the full baseline/exception contract.
- Inspect coverage output for retained owned packages/suites and inspect CI logs/configuration for both browser modes.
- Validate workflow YAML and shell/Python syntax; use the same check entrypoint locally and in CI.

## Evidence

- `tools/check.sh` passed in full (`/tmp/arez-check-complete-t25.log`): 22 build targets, all 40 default test targets, eight optimized browser targets, all 31 JVM coverage targets, artifacts/isolated consumers, 13,911 site links and disposable release checks.
- The 31 JVM targets ran 2,301 TestNG invocations; the site adds 14 and browser suites cover 22 cases per mode. Strict Java 17 example/test source targets are explicitly built. `//:all_tests` contains JVM, browser and site suites.
- Ahab 0.4.0's full graph passes in fastbuild and opt, with empty violations and 16 exact browser exceptions. Initial analysis found four host-shell smoke preview genrules; replacing unused preview machinery with direct Closure bundles removed them rather than broadening exceptions. `:hermeticity.explain` reports all checks passed.
- A negative probe removed only the site's reproducibility declaration: baseline enforcement rejected `unknown_program`. Restoring its build-mode declaration passed. Archive/formatter declarations and browser reasons were inspected; report is T25-ahab-report.json.
- Coverage in a separate dbg output configuration contains 245 owned source files, 12,298 lines (11,315 hit) and 6,059 branches (4,917 hit), with all six tested product module prefixes and 31 per-target reports. There are no percentage gates. Copying the report replaces an existing read-only copy and creates writable local output (0644); the repeat-run failure exposed and corrected this bug.
- CI YAML loads, calls the same tools/check.sh using Java 21/Bazel setup, includes both browser modes through that entrypoint and retains coverage/verification artifacts. No Ruby/Node setup or Buildr commands remain in active CI. Shell/Python syntax, format/Buildifier and documentation/plan validators pass. The GitHub-hosted Linux job has not been executed from this local branch.
- Pins: Bazel 9.3.0, rules_java 9.9.0, Ahab 0.4.0, Buildifier 10.0.1, Palantir rules 0.1.1, target Java 17, processor/J2CL runtime 21 and execution tools 25; browser/compiler pins are recorded in T19–T20.
