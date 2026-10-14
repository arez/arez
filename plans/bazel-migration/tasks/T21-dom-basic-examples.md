# T21 — Run location, size and media-query examples

- Status: complete
- Blocked by: T20
- Spec coverage: R03, R04, R05, R09, R10, AC03, AC04, AC05, AC09, AC10

## Delivers

Three DOM applications preview and pass reactive behavior checks in both browser modes.

## Acceptance criteria

- [x] Convert BrowserLocationExample, WindowSizeExample and MediaQueryExample to the shared J2CL bootstrap/preview convention.
- [x] Migrate these example and browser-test Java sources to JSpecify and include every source in explicit strict Java 17 compilation targets using the same owned-source policy; declare packaged DOM/core JVM dependencies.
- [x] Use public packaged DOM/core J2CL inputs and the actual application entrypoint in each test.
- [x] Check initial output and meaningful location/hash/size/media-query changes using deterministic browser controls.
- [x] Test cleanup/disposal where the example creates releasable subscriptions; avoid extending runtime APIs solely for tests.

## Validation

- Build and browser-test all three application targets in development and ADVANCED production modes.
- Build strict Java 17 targets for all three source/test sets and inspect policy options; include them in //extras/dom-example:java as this slice lands.
- Load each local preview page; inspect rendered updates and console errors.
- Confirm all three case rows appear in the browser inventory.

## Evidence

- Converted all three applications to exported J2CL bootstraps, retained their authored HTML controls/output and location console messages, and removed their GWT modules/nocache loaders. A small `ExampleDom` facade centralizes nullable element lookup and removable button listeners; each app owns and disposes its observers/model.
- [Browser evidence and inventory](../T21-browser-results.json): location runs four cases (empty bootstrap, all route buttons, hash/invalid-route reset, disposal), size runs two (resize, disposal), and media runs three (match event, query rebind, disposal). All pass in development BUNDLE and ADVANCED production. Tests call the preview bootstrap, check actual mode settings, control event/query inputs and assert native listener cleanup. All three rows are in `//:browser_tests`, alongside promise; the four-suite matrix passes in both modes.
- [Strict policy/source evidence](../T21-compiler-policy.json): the shared facade and every app compile with `--release 17`, Werror and the full owned Error Prone/JSpecify NullAway policy. All emitted classes are bytecode 61. J2CL consumes archives assembled from these checked Java targets and uses packaged public core/DOM source inputs.
- Initial empty-hash browser execution demonstrated a `StringIndexOutOfBoundsException` at `BrowserLocation.getHash()`. Added the empty check. ADVANCED diagnosed undeclared global listener functions and a narrow HashChangeEvent callback; the private native facade now uses Window methods and generic Event callbacks. User-visible behavior is recorded in Unreleased. No public runtime API was added for testing.
- The media fake exposes a typed, quoted MediaQueryList facade so ADVANCED preserves its native ABI. Window/hash listeners are instrumented and restored; no checker exemption, real permission, network request or timing sleep is used.
- Ran each local preview on an ephemeral loopback port and inspected the generated page in the in-app browser: location displayed both empty route fields and all buttons, size displayed `1280 x 720`, media displayed Wide and the 600px query. All had zero console warnings/errors. Temporary tabs/servers were closed.
- `bazel test //:browser_tests //extras/dom:tests` passes the four development browser suites and retained DOM JVM cases; `bazel test -c opt //:browser_tests` passes all four production browser suites. Both-mode application builds, strict Java compilation, Palantir and Buildifier checks pass.
- Rebuilt publication outputs in independent output bases after the required runtime fix. [Artifact evidence](../T21-artifacts.json) records matching hashes for all 32 artifacts and aggregate API docs, plus a passing isolated Java 21 processor/runtime consumer.
- Added exact-action HTML/no-sandbox exception rows for the three browser runners; full Ahab validation remains T25. Development commands, whitespace, plan and domain-documentation links pass.
