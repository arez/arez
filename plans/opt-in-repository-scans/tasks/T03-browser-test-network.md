# T03 — Enable networking for the browser test runner

- Spec coverage `R5`, `R6`, `AC5`, `AC6`

Restore the default macOS browser test run. Selenium checks a wildcard bind while
selecting its local server port. Add the network requirement only to the final
WebDriver test targets in a small patch to the pinned Closure rules. Keep the
pinned tools and the existing test macro. Replace obsolete Ahab allowances for
those runners with exact exceptions for the new test action requirement.

## Acceptance and validation

- Confirm the temporary local override is absent. Inspect the Bazel action graph
  to verify that only the ten WebDriver TestRunner actions gain networking.
  Validate that compilation and other test runners have no new network requirement.
- Run the focused core browser target with Bazel's default settings and `--nocache_test_results`, then
  `tools/check.sh`. The latter must pass both browser modes and both Ahab checks.
- After independent implementation review, remove the exact plan tree and
  remaining probe scratch. Report the verified compiler cause and sizes to the
  user before requesting assessment. Publish and verify the PR only after the
  checkpoint is satisfied.

## Evidence

- The first `tools/check.sh` run stopped at `//:all_tests`. All 36 JVM and site targets passed. All ten browser targets failed before launching Chromium when Selenium's `PortProber` could not bind a server port under Bazel's macOS sandbox. Host binds on IPv4 and IPv6 succeeded.
- `bazel test //core/src/test/js:browser_test --sandbox_default_allow_network=true --nocache_test_results --test_output=all` passed in the Darwin sandbox. The runner bound localhost and ran all four browser scenarios. Upstream's default Linux gate passed at the same base. [Upstream Verify run](https://github.com/arez/arez/actions/runs/41121894344)
- The temporary override was used only to validate the diagnosis. The required delivery gate uses the targeted fix with default settings.
- The narrow Closure rules patch separates the final WebDriver rule's `test_tags` from its common tags. Arez opts in from `browser_test()`. The patch defaults to an empty list for other callers, including those using the generated downstream configuration.
- With no local override, `bazel test //core/src/test/js:browser_test --nocache_test_results --test_output=errors` passed in the Darwin sandbox.
- `bazel aquery '//...' --output=jsonproto --include_commandline=false --include_artifacts=false` returned 1,318 actions. The only ten with `requires-network` were the expected WebDriver TestRunner targets, matching the ten Ahab exceptions.
- `tools/check.sh` exited successfully with default settings. Both Ahab modes passed. All 46 default test targets and all ten optimized browser targets passed. Packaged JVM and J2CL consumers, coverage, site, and release bundle checks passed.
