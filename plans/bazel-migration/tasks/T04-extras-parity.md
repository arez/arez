# T04 — Build extras and run their JVM tests

- Status: pending
- Blocked by: T03
- Spec coverage: R01, R02, R06, AC01, AC02, AC06

## Delivers

All four extras libraries build under Bazel and existing DOM/spytools JVM tests run.

## Acceptance criteria

- [ ] Expose //extras/dom, //extras/promise, //extras/spytools and //extras/testng, plus DOM/spytools :tests.
- [ ] Preserve Arez annotation processing, generated sources, native JS/resources, non-nullability annotations and runtime properties.
- [ ] Record that promise and TestNG extras have no existing JVM suites; do not invent empty tests to imply coverage.
- [ ] Include extras in the retained JVM build inventory.

## Validation

- bazel build //extras/dom //extras/promise //extras/spytools //extras/testng — verifies all extras products and generated classes.
- bazel test //extras/dom:tests //extras/spytools:tests --test_output=errors — retains existing JVM behavior.

## Evidence

- pending
