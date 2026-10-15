# Development checks

Use Bazel to build and test the repository. Run `tools/check.sh` before submitting
changes; it runs the same checks as CI, including formatting, hermeticity, tests,
coverage, publication artifacts and the website.

For focused work:

```sh
bazel test //:jvm_tests
bazel test //processor:all_tests
bazel test //persist/processor:all_tests
tools/java_format.sh write
tools/java_format.sh check
bazel run //tools/buildifier:buildifier_check
```

Runtime builds and generated-output tests enforce Error Prone and JSpecify-mode
NullAway. Javac warnings fail the build; diagnostic tests use plain javac. JVM
tests enable assertions. See the [processor nullability
specification](specs/processor-nullability.md) when changing nullness contracts.

## Processor goldens

Generated-output tests compare the exact emitted-file set in both `expected` and
`expectedFormatted`. Run the family that owns the changed fixture, then update
its goldens:

```sh
bazel test //processor/src/test/java/arez/processor:observable_goldens
tools/update_goldens.sh arez observable
tools/update_goldens.sh persist types
```

The family names and fixture membership are declared in the [Arez test
package](../processor/src/test/java/arez/processor/BUILD.bazel) and [Persist test
package](../persist/processor/src/test/java/arez/persist/processor/BUILD.bazel).
Diagnostic expectations stay beside their Java test cases. The aggregate suites
also run warning and forked-javac formatter checks.

The updater regenerates both modes in a temporary directory, copies changed
outputs back and reruns the selected family. Declare new golden paths in the
owning BUILD file first. Review the generated diff before committing.

## Runtime fixtures and invariant codes

```sh
tools/update_runtime_fixtures.sh core-invariants
tools/update_runtime_fixtures.sh integration arez.integration.NativeComponentsIntegrationTest.inspectComponentViaSpy
```

The core updater runs the entire suite to collect invariant messages. The
integration updater runs one spy-event scenario and updates only its JSON file.
Both rerun their checks after copying the results back.

Use `python3 tools/error_codes.py unused`, `new` or `next` to inspect or allocate
invariant codes. The commands use the maintained message inventory; `next` reuses
a retired code before allocating a new one.

## Packaged downstream targets

[Project setup](project_setup.md#configure-bazel) describes the public JVM imports,
processor plugins and J2CL libraries. They use the [publication
archives](adr/0002-publication-artifact-boundary.md), so changes to packaging must
also work for downstream consumers.

```sh
bazel build //tools/release:maven_artifacts //tools/release:site_javadoc
python3 tools/release/check_artifacts.py
python3 tools/check_bazel_consumer.py
```

These checks inspect the archives and compile and run consumers with the packaged
processors. The Bazel consumer runs in a separate workspace to check the public
labels and required root configuration.

## Local browser examples

```sh
python3 tools/browser/preview.py //extras/promise-example:app
python3 tools/browser/preview.py //extras/promise-example:app --production
bazel test //extras/promise-example:browser_test
bazel test -c opt //extras/promise-example:browser_test
```

The preview listens on loopback port 8000; use `--port` to choose another port.
Default builds enable development checks. `--production` selects the optimized
production application; `-c opt` selects the same mode for Bazel tests.

The DOM examples use targets under `//extras/dom-example`:

| Example | Preview target | Browser test |
| --- | --- | --- |
| Browser location | `:location_app` | `:location_browser_test` |
| Window size | `:size_app` | `:size_browser_test` |
| Media query | `:media_app` | `:media_browser_test` |
| Document visibility | `:visibility_app` | `:visibility_browser_test` |
| Geolocation | `:geo_app` | `:geo_browser_test` |
| Idle status | `:idle_app` | `:idle_browser_test` |
| Network status | `:network_app` | `:network_browser_test` |

For example, run `python3 tools/browser/preview.py //extras/dom-example:geo_app`.
JavaScript entry modules sit beside their Java examples as `.entry.js` files.
Browser tests control external APIs and time so they can run without live network
services or permissions.

Run the complete browser matrix with `bazel test //:browser_tests` and
`bazel test -c opt //:browser_tests`. Build `//extras/dom-example:java` and
`//extras/promise-example:java` to check the example Java sources directly.

## Complete verification and coverage

`tools/check.sh` runs the complete gate. `//:all_tests` collects JVM, browser and
site tests; `//:jvm_tests` and `//:browser_tests` support focused runs.

JVM coverage writes `reports/coverage/jvm.lcov` and a JSON summary. Reports must
include the tested product modules and exercised lines; there are no percentage
thresholds. CI retains the coverage and artifact reports under `reports/`.

Ahab checks whether build actions declare their inputs and tools. Run
`bazel run //tools/ahab:hermeticity.check` to enforce the baseline or
`bazel run //tools/ahab:hermeticity` to inspect the graph. Review findings before
changing the baseline, exceptions or reproducibility declarations in
`tools/ahab/`; `:hermeticity.explain` explains the baseline and
`:hermeticity.update` updates it.

## Documentation and releases

`tools/site.sh build` writes `reports/site`; `tools/site.sh check` checks local
files and fragments. `tools/site.sh serve` builds and serves the site on loopback
port 3000. Rebuild and refresh after editing documentation. Source snippets come
from the compiled examples, including generated Java. External links are not
checked by the site tool.

See [Releasing Arez](releasing.md) for release preparation and publication.
