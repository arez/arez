# T20 — Run and test the promise J2CL example

- Status: pending
- Blocked by: T19
- Spec coverage: R03, R04, R05, R09, R10, R13, AC03, AC04, AC05, AC09, AC10, AC13

## Delivers

The promise application previews locally and passes deterministic development/production Chromium tests through its actual bootstrap.

## Acceptance criteria

- [ ] Replace GWT EntryPoint/nocache loading with an exported bootstrap and Closure entry module using public packaged promise/core J2CL inputs.
- [ ] Migrate example and browser-test Java sources to JSpecify by intent, including nullable browser/native results. Add an explicit strict Java 17 compile target for every such source using the same Error Prone/NullAway policy and packaged JVM imports as the other owned Java.
- [ ] Introduce the small shared example preview/server and browser test glue needed by subsequent DOM slices, with pinned browser/driver downloads.
- [ ] Exercise pending, fulfilled and rejected promise states using controlled responses; avoid live MDN fetches in tests and invoke the same app bootstrap as preview.
- [ ] Define development and ADVANCED production configurations with appropriate Arez/Braincheck settings.
- [ ] Add only required scoped browser runner/root-relative HTML Ahab exceptions, preserving reasons from the verified Rose pattern.

## Validation

- Build the promise application in both default and -c opt modes.
- Build //extras/promise-example:java (including browser-test compile targets) and inspect its compiler action options — proves owned example/test code is JSpecify-checked under the strict Java 17 policy rather than merely transpiled.
- Run the promise browser target in both modes; verify pending/fulfillment/rejection and displayed state without live-service access.
- Run the local preview command and load its generated HTML/JS; inspect browser console for bootstrap errors.
- Check browser artifact pins and exception scope.

## Evidence

- pending
