# T22 — Run visibility, geolocation, idle and network examples

- Status: pending
- Blocked by: T20
- Spec coverage: R03, R04, R05, R09, R10, AC03, AC04, AC05, AC09, AC10

## Delivers

The remaining four DOM applications have deterministic local preview/browser verification in both modes.

## Acceptance criteria

- [ ] Convert DocumentVisibilityExample, GeoPositionExample, IdleStatusExample and NetworkStatusExample to J2CL bootstraps.
- [ ] Migrate these example and browser-test Java sources to JSpecify and explicit strict Java 17 source/test compilation. Complete //extras/dom-example:java so its source inventory covers all seven examples and every browser-test Java class.
- [ ] Control visibility events, geolocation callbacks/errors/permissions, idle scheduling and online/offline notifications without real sensors/services or sleeping.
- [ ] Exercise actual example rendering/reactive updates, relevant failure paths and cleanup where applicable.
- [ ] Complete the seven-DOM-plus-one-promise inventory and expose //:browser_tests; ensure no example is merely compile-tested.

## Validation

- Build and browser-test these four applications in default and -c opt modes.
- Build the four strict Java source/test targets; once T21 is complete, build //extras/dom-example:java and compare its inventory with all seven applications/browser suites.
- Once T21 is complete, bazel test //:browser_tests and bazel test -c opt //:browser_tests — mandatory all-eight-by-two gate.
- Run local preview for all four and inspect bootstrap/console output; verify tests contain no live geolocation/network calls or timing sleeps.

## Evidence

- pending
