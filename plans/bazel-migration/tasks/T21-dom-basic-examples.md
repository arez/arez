# T21 — Run location, size and media-query examples

- Status: pending
- Blocked by: T20
- Spec coverage: R03, R04, R05, R09, R10, AC03, AC04, AC05, AC09, AC10

## Delivers

Three DOM applications preview and pass reactive behavior checks in both browser modes.

## Acceptance criteria

- [ ] Convert BrowserLocationExample, WindowSizeExample and MediaQueryExample to the shared J2CL bootstrap/preview convention.
- [ ] Migrate these example and browser-test Java sources to JSpecify and include every source in explicit strict Java 17 compilation targets using the same owned-source policy; declare packaged DOM/core JVM dependencies.
- [ ] Use public packaged DOM/core J2CL inputs and the actual application entrypoint in each test.
- [ ] Check initial output and meaningful location/hash/size/media-query changes using deterministic browser controls.
- [ ] Test cleanup/disposal where the example creates releasable subscriptions; avoid extending runtime APIs solely for tests.

## Validation

- Build and browser-test all three application targets in development and ADVANCED production modes.
- Build strict Java 17 targets for all three source/test sets and inspect policy options; include them in //extras/dom-example:java as this slice lands.
- Load each local preview page; inspect rendered updates and console errors.
- Confirm all three case rows appear in the browser inventory.

## Evidence

- pending
