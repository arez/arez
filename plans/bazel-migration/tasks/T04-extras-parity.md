# T04 — Build extras and run their JVM tests

- Status: complete
- Blocked by: T03
- Spec coverage: R01, R02, R06, AC01, AC02, AC06

## Delivers

All four extras libraries build under Bazel and existing DOM/spytools JVM tests run.

## Acceptance criteria

- [x] Expose //extras/dom, //extras/promise, //extras/spytools and //extras/testng, plus DOM/spytools :tests.
- [x] Preserve Arez annotation processing, generated sources, native JS/resources, non-nullability annotations and runtime properties.
- [x] Record that promise and TestNG extras have no existing JVM suites; do not invent empty tests to imply coverage.
- [x] Include extras in the retained JVM build inventory.

## Validation

- bazel build //extras/dom //extras/promise //extras/spytools //extras/testng — verifies all extras products and generated classes.
- bazel test //extras/dom:tests //extras/spytools:tests --test_output=errors — retains existing JVM behavior.

## Evidence

- All four product builds passed with existing Arez-generated classes.
- DOM: 2 tests passed; spytools: 9 tests passed; zero failures/skips.
- Promise and TestNG extras have no existing JVM test source trees; their absence is explicit in [inventory](../extras-test-inventory.json). Browser coverage is owned by T19–T20.
- Inventory records all 28 product sources, seven test sources and four concrete test classes; SHA-256 comparisons against original baseline passed unchanged.
- Buildifier and generated dependency checksum checks passed. Native runtime code is inline Java/JsInterop in these modules; GWT descriptors remain for the later retirement task.
