# T25 — Run the full check entrypoint in CI

- Status: pending
- Blocked by: T21, T22, T23, T24
- Spec coverage: R01, R03, R05, R06, R07, R08, R09, R10, R11, R13, R14, AC01, AC03, AC05, AC06, AC07, AC08, AC09, AC10, AC11, AC13, AC14

## Delivers

One local/CI command verifies the retained graph, browser matrix, release consumers, site and reviewed hermeticity baseline.

## Acceptance criteria

- [ ] Create tools/check.sh and CI using Java/Bazel and documented Python/Bash tools, removing Ruby/Node setup and Buildr commands from active CI.
- [ ] Run Buildifier, formatter, Ahab, complete JVM builds/tests, strict Java 17 example/browser-test builds (//extras/promise-example:java and //extras/dom-example:java), packaged consumer/source checks, all browser tests in both modes and site checks. Include every owned example/test source in the strict checker gate.
- [ ] Port Ahab check/update/explain targets into tools/ahab; inspect every finding, declare custom tool reproducibility and retain only justified target-scoped browser exceptions.
- [ ] Generate nonempty coverage reports for retained JVM suites and publish/retain them as CI artifacts without new percentage thresholds.
- [ ] Expose //:all_tests containing retained JVM tests and browser tests; also maintain explicit suite aliases so the optimized browser invocation cannot be omitted.
- [ ] Record actual tool versions/pins and CI command paths; do not accept unexplained baseline additions as fixes.

## Validation

- tools/check.sh — mandatory complete CI-equivalent gate.
- bazel run //tools/ahab:hermeticity.check and :hermeticity.explain — review the full baseline/exception contract.
- Inspect coverage output for retained owned packages/suites and inspect CI logs/configuration for both browser modes.
- Validate workflow YAML and shell/Python syntax; use the same check entrypoint locally and in CI.

## Evidence

- pending
