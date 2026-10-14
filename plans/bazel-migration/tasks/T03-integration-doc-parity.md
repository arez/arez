# T03 — Compile generated integrations and documentation examples

- Status: pending
- Blocked by: T02
- Spec coverage: R01, R02, R06, AC01, AC02, AC06

## Delivers

Bazel annotation processing compiles the main integration suite and documentation examples, and checks spy-event fixtures.

## Acceptance criteria

- [ ] Expose //integration-tests:tests and //doc-examples using explicit internal Arez/Sting plugins and formatter exports.
- [ ] Declare event JSON and other integration resources as runfiles; preserve development configuration and assertions.
- [ ] Retain every integration class and compilation path, including generated helpers and optional Sting integration.
- [ ] Do not perform the GWT bootstrap/support cut yet; any temporary GWT dependency needed for baseline examples is owned by T26 for removal.

## Validation

- bazel build //doc-examples — compiles source and generated examples used by the site.
- bazel test //integration-tests:tests --test_output=errors — checks runtime behavior and spy-event expectations.
- Compare the test-class/resource inventory against the original module — demonstrates complete retained coverage.

## Evidence

- pending
