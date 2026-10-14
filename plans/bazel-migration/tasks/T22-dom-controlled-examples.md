# T22 — Run visibility, geolocation, idle and network examples

- Status: complete
- Blocked by: T20
- Spec coverage: R03, R04, R05, R09, R10, AC03, AC04, AC05, AC09, AC10

## Delivers

The remaining four DOM applications have deterministic local preview/browser verification in both modes.

## Acceptance criteria

- [x] Convert DocumentVisibilityExample, GeoPositionExample, IdleStatusExample and NetworkStatusExample to J2CL bootstraps.
- [x] Migrate these example and browser-test Java sources to JSpecify and explicit strict Java 17 source/test compilation. Complete //extras/dom-example:java so its source inventory covers all seven examples and every browser-test Java class.
- [x] Control visibility events, geolocation callbacks/errors/permissions, idle scheduling and online/offline notifications without real sensors/services or sleeping.
- [x] Exercise actual example rendering/reactive updates, relevant failure paths and cleanup where applicable.
- [x] Complete the seven-DOM-plus-one-promise inventory and expose //:browser_tests; ensure no example is merely compile-tested.

## Validation

- Build and browser-test these four applications in default and -c opt modes.
- Build the four strict Java source/test targets; once T21 is complete, build //extras/dom-example:java and compare its inventory with all seven applications/browser suites.
- Once T21 is complete, bazel test //:browser_tests and bazel test -c opt //:browser_tests — mandatory all-eight-by-two gate.
- Run local preview for all four and inspect bootstrap/console output; verify tests contain no live geolocation/network calls or timing sleeps.

## Evidence

- Converted visibility, geolocation, idle and network to exported J2CL bootstraps and removed their GWT/nocache loaders. Visibility/idle retain console messages and now render them in the page. Geo starts watching on its button, allows one observer, renders nullable position/error state and cleans up the watch/button/observer/model. Removed its debugger trap and Node-only preview instructions.
- [Eight-by-two browser matrix](../T22-browser-results.json): all seven DOM suites plus promise pass in BUNDLE development and ADVANCED production (22 cases per mode). The four new suites each have two cases. They invoke the actual preview bootstrap and check rendering, mode flags and cleanup.
- Visibility controls visible/hidden/prerender notifications; network controls online/offline properties/events. Geo controls native callbacks for loading, nullable coordinate data, permission denial, timeout and recovery, and checks repeated clicks create one watch and disposal clears it. Idle drives Date.now and timer callbacks with a manual clock, testing 1999/2000ms boundaries, activity reset, all seven event bindings and pending timer cleanup. Tests use no real sensors/permissions/network or timing sleeps.
- Listener instrumentation now counts bindings per event type and listener, so the same callback registered for several events cannot hide a cleanup error. All prior basic-DOM suites pass with the stronger tracker.
- [Compiler/source inventory](../T22-compiler-policy.json): all eight DOM example/facade Java files plus shared Java browser assertions match declared dependency membership. Four new strict compilation actions use Java 17, Werror and the complete Error Prone/JSpecify-mode NullAway policy; every emitted class is bytecode 61. J2CL consumes their checked source archives. `//extras/dom-example:java` covers all seven examples and support.
- ADVANCED exposed the existing unqualified global event calls in IdleStatus/NetworkStatus. Bound the private native methods to window.addEventListener/window.removeEventListener; this qualified global method pattern is also used by the pinned J2CL System.Date.now binding. Both modes and retained DOM JVM tests pass; Unreleased records the fix.
- Ran all four local preview commands on ephemeral loopback ports and inspected initial rendering and console output in the in-app browser. Visibility showed VISIBLE, Geo showed initial -2 and its watch button without requesting permission, idle showed Active, and network showed Online. Each had zero console warnings/errors. Temporary tabs and servers were closed.
- Built all four applications in both modes, complete strict sources and Palantir. Buildifier, JSON/plan/whitespace and documentation links pass. Added exact-target no-sandbox/HTML exception rows; complete Ahab checking remains T25.
- Rebuilt all publication outputs in two output bases after the listener fixes. [Artifact evidence](../T22-artifacts.json) confirms all 32 artifacts and aggregate API docs match, plus isolated Java 21 processors/runtime consumers pass.
- Knowledge-sweep follow-up: inspection found GeoPosition.onSuccess supplies longitude as Position.speed. `git show` confirms the identical copy error at the original baseline. It does not affect the example's displayed latitude/longitude; changing that unrelated runtime behavior is outside this migration and T26 must classify it alongside the prior Persist findings.
