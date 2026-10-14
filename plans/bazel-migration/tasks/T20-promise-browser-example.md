# T20 — Run and test the promise J2CL example

- Status: complete
- Blocked by: T19
- Spec coverage: R03, R04, R05, R09, R10, R13, AC03, AC04, AC05, AC09, AC10, AC13

## Delivers

The promise application previews locally and passes deterministic development/production Chromium tests through its actual bootstrap.

## Acceptance criteria

- [x] Replace GWT EntryPoint/nocache loading with an exported bootstrap and Closure entry module using public packaged promise/core J2CL inputs.
- [x] Migrate example and browser-test Java sources to JSpecify by intent, including nullable browser/native results. Add an explicit strict Java 17 compile target for every such source using the same Error Prone/NullAway policy and packaged JVM imports as the other owned Java.
- [x] Introduce the small shared example preview/server and browser test glue needed by subsequent DOM slices, with pinned browser/driver downloads.
- [x] Exercise pending, fulfilled and rejected promise states using controlled responses; avoid live MDN fetches in tests and invoke the same app bootstrap as preview.
- [x] Define development and ADVANCED production configurations with appropriate Arez/Braincheck settings.
- [x] Add only required scoped browser runner/root-relative HTML Ahab exceptions, preserving reasons from the verified Rose pattern.

## Validation

- Build the promise application in both default and -c opt modes.
- Build //extras/promise-example:java (including browser-test compile targets) and inspect its compiler action options — proves owned example/test code is JSpecify-checked under the strict Java 17 policy rather than merely transpiled.
- Run the promise browser target in both modes; verify pending/fulfillment/rejection and displayed state without live-service access.
- Run the local preview command and load its generated HTML/JS; inspect browser console for bootstrap errors.
- Check browser artifact pins and exception scope.

## Evidence

- Exported `ObservablePromiseExample.bootstrap()`, removed its GWT module/nocache loader, and added the Closure entry and generated HTML/JS bundle. The preview fetches its own `index.html`, preserving the response/status demonstration with local input.
- [Browser results](../T20-browser-results.json): both default BUNDLE development and `-c opt` ADVANCED production suites pass five cases (pending, response fulfillment, error rejection, null fulfillment and null rejection). Each invokes the same Java bootstrap as preview, asserts rendered text and actual Arez/Braincheck checks, and verifies observer/model disposal. Native Promises are controlled without network access or timing sleeps; the returned rejection branch is captured and its exact rejection is asserted.
- Both preview commands were run on ephemeral loopback ports and inspected in the in-app browser. Each rendered `Promise State: FULFILLED - Response: 200: OK` with zero console warnings/errors. Temporary tabs and servers were closed.
- [Compiler evidence](../T20-compiler-policy.json): application and Java browser assertions pass the full owned Error Prone/NullAway policy with `--release 17`, `-Werror`, JSpecify mode and assertion modeling. Their jars contain six application classes and one support class, all bytecode 61. J2CL consumes archives assembled from those checked Java targets; browser builds therefore depend on the strict Java 17 gate.
- Production application and test actions both use ADVANCED with production Arez/Braincheck settings. Closure's jsunit harness requires `goog.DEBUG=true`; its test-only setting is retained independently of the asserted production application settings. An observed initialization error established this requirement.
- Braincheck 1.35's sources archive omits native JS while its binary jar contains the identical Java sources and both JS companions. The explicit J2CL input now uses that complete jar; tests assert effective Braincheck checks in both modes. All five public libraries and all four promise/spytools smoke variants rebuild successfully with this correction.
- [Browser pins and scope](../T20-browser-pins.json): rules_webtesting 0.4.1 uses checksummed browser manifest 0.3.4; Chromium and matching driver 123.0.6262.0 ran locally. Action inspection confirms only the TestRunner requests `no-sandbox` and receives `/gen_html_browser_test.html`; two exact-target Ahab exceptions record those reasons. Full Ahab execution remains T25.
- Shared browser bundle/test rules, Java assertions and `tools/browser/preview.py` are declared/documented for the remaining DOM slices. Root `//:browser_tests` currently includes promise and will expand with those tasks.
- Palantir, Buildifier, JSON/Python syntax, whitespace, plan and domain-documentation link checks pass. The development documentation includes build, both-mode tests and preview commands.
