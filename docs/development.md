# Development checks

Bazel compiles shared processor test support once. Generated-output tests use
feature families as the cache boundary, keeping JVM startup costs bounded while
allowing fixture and golden edits to invalidate their owning family. Each family
checks the exact emitted-file set in both `expected` and `expectedFormatted`.
The test providers and BUILD files own membership; diagnostic message fragments
remain next to their Java scenarios.

Run all retained JVM tests with `bazel test //:jvm_tests`. The processor aggregates
are `//processor:all_tests` and `//persist/processor:all_tests`. Each keeps plain
javac diagnostics, warning promotion and forked-javac formatter checks separate
from generated-output families. Inherited, multi-source and multi-output Arez
scenarios use `basic_scenarios_goldens` and `scenarios_goldens`; Persist also runs the Arez
processor and excludes its outputs when comparing Persist goldens.

The `basic_` families cover components, lifecycle hooks and runtime references.
Their paired families cover configuration, auto-observation, disposal notifiers
and identity references. Plain-javac diagnostics use `basic_diagnostics` and
`remaining_diagnostics`; `diagnostics` aggregates both.

Choose a family to run or update:

| Processor | Families | Target prefix |
| --- | --- | --- |
| Arez | component, action, observable, memoize, observe, lifecycle, relationships, metadata, interop, scenarios, jspecify; basic_component, basic_action, basic_observe, basic_lifecycle, basic_metadata, basic_scenarios | `//processor/src/test/java/arez/processor:` |
| Persist | model, types | `//persist/processor/src/test/java/arez/persist/processor:` |

Append `_goldens` to the family name for its target. For example:

```sh
bazel test //processor/src/test/java/arez/processor:observable_goldens
tools/update_goldens.sh arez observable
tools/update_goldens.sh persist types
```

The updater copies declared runfiles to a disposable directory, regenerates both
output modes there, copies changed goldens back, reruns the selected family and
shows the diff. It rejects aggregate selections. Declare and review new golden
paths in the owning BUILD file before updating them; the existing exact-output
assertions catch unexpected emissions. Review every generated change before
committing it.

Runtime fixtures have their own updater:

```sh
tools/update_runtime_fixtures.sh core-invariants
tools/update_runtime_fixtures.sh integration arez.integration.NativeComponentsIntegrationTest.inspectComponentViaSpy
```

Core invariant messages require the complete core suite so the collector sees all
messages. Spy-event updates select exactly one integration method and copy back
only its JSON file. Both use disposable copies of declared fixture inputs and
rerun the corresponding check.

Java runtime sources use the pinned Sting Error Prone policy and JSpecify-mode
NullAway. Checks are errors and javac warnings fail the build. NullAway also
reasons about the existing Java assertions; Bazel JVM tests enable assertions.
A valid runtime call must satisfy those internal invariants even when production
assertions are disabled. Spy-only methods and initialized generic caches have
small documented exceptions where their lifecycle contract supplies the proof.


## Packaged downstream targets

The root labels import the assembled release jars and attach their complete source
archives. They match the inputs published to Maven Central:

| Bazel label | Maven artifact |
| --- | --- |
| `@arez//:core` | `arez-core` |
| `@arez//:processor` | `arez-processor` |
| `@arez//:dom` | `arez-extras-dom` |
| `@arez//:promise` | `arez-extras-promise` |
| `@arez//:spytools` | `arez-extras-spytools` |
| `@arez//:testng` | `arez-extras-testng` |
| `@arez//:persist_core` | `arez-persist-core` |
| `@arez//:persist_processor` | `arez-persist-processor` |

Use `@arez//:arez_processor_plugin` and
`@arez//:arez_persist_processor_plugin` in the consumer's `plugins` list.
Both plugins load the assembled shaded processor jars. Internal labels such as
`//core` and `//processor:plugin` remain repository implementation targets.

A downstream MODULE file can declare `bazel_dep(name = "arez")` and use
`git_override(module_name = "arez", remote = "https://github.com/arez/arez.git",
commit = "<full approved commit SHA>")`. Replace the placeholder with an
immutable revision available in the remote. For local development, use
`local_path_override(module_name = "arez", path = "/absolute/path/to/arez")`
in the downstream workspace. The Arez checkout itself uses remotely pinned
Proton/Sting modules without local overrides.

Runtime and generated classes target Java 17. Processors require Java 21 or newer.
When compiling against inherited classes, retain parameter names with
`-parameters` and use `-XDaddTypeAnnotationsToSymbol=true` so javac exposes
compiled type-use annotations to processors. Declare JSpecify on the application
classpath. The processorpath needs only the assembled Arez/Persist processor jars;
Proton, Javapoet and the formatter runtime are shaded into those jars.

Standalone javac formatting requires the six exports below (Bazel's Java compiler
already exposes these internals):

```text
-J--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED
-J--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED
-J--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED
-J--add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED
-J--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED
-J--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
```

Build the distribution with `bazel build //tools/release:maven_artifacts` and run
`python3 tools/release/check_artifacts.py`. The checker compiles an inherited
JSpecify base separately, then compiles and runs a tiny consumer with the
published runtime jars and isolated Java 21 processorpaths. It checks reactive
updates, nullable generic/array contracts, persisted state and null removal,
disposal, and the JVM-compatible extras APIs. Browser-native behavior is checked
by the J2CL browser suites.

The browser labels `@arez//:core-j2cl`, `@arez//:promise-j2cl`,
`@arez//:dom-j2cl`, `@arez//:spytools-j2cl` and `@arez//:persist-core-j2cl`
compile those same release source archives, including generated Java and native
JavaScript configuration companions. JSpecify annotations are imported as
bytecode. JVM configuration, logging and test utilities marked
`GwtIncompatible` are stripped by J2CL; that annotation's simple name is a
transpiler convention. It does not introduce a GWT build dependency.

The J2CL Java compiler uses a pinned Java 21 runtime. The retained compilation
smokes are `//extras/promise:compile_smoke` and
`//extras/spytools:compile_smoke`. Their `_dev` variants exercise development
Closure output; the base targets compile ADVANCED production output.

## Local browser examples

Run the promise example with:

```sh
python3 tools/browser/preview.py //extras/promise-example:app
python3 tools/browser/preview.py //extras/promise-example:app --production
bazel build //extras/promise-example:java
bazel test //extras/promise-example:browser_test
bazel test -c opt //extras/promise-example:browser_test
```

The preview binds loopback on port 8000; use `--port` to select another port.
Its fetch reads the local `index.html`. The bootstrap and the browser suite use
the same exported Java method. Tests replace fetch with controlled native
Promises and check pending, fulfillment, rejection, null values and disposal.

Default browser builds use Closure BUNDLE output with Arez/Braincheck development
checks. `-c opt` uses ADVANCED output and production application settings. Closure's
jsunit harness retains `goog.DEBUG=true` in both test modes; the test asserts the
application's actual check settings independently. Chromium and its matching
driver are checksum-pinned by rules_webtesting 0.4.1's browser manifest 0.3.4.
Browser execution runs outside the Bazel sandbox for its native launcher and
loopback server; its compilation remains sandboxed. The Ahab exceptions name
only that test action and its generated root-relative HTML argument.

The location, window-size and media-query examples use the same commands under
`//extras/dom-example`:

| Example | Preview target | Browser test |
| --- | --- | --- |
| Browser location | `:location_app` | `:location_browser_test` |
| Window size | `:size_app` | `:size_browser_test` |
| Media query | `:media_app` | `:media_browser_test` |
| Document visibility | `:visibility_app` | `:visibility_browser_test` |
| Geolocation | `:geo_app` | `:geo_browser_test` |
| Idle status | `:idle_app` | `:idle_browser_test` |
| Network status | `:network_app` | `:network_browser_test` |

For example, run `python3 tools/browser/preview.py //extras/dom-example:location_app`.
Build `//extras/dom-example:java` for the complete strict Java 17 source gate.
Those checked source archives are also the J2CL inputs. `//:browser_tests` collects
the browser suites as they land. Location tests cover route buttons, hash events,
invalid-route reset and the empty-hash bootstrap. Size/media tests drive explicit
events and query responses; all three verify that disposal removes subscriptions.

Visibility and network tests control the browser properties and notifications.
Geolocation starts on the watch button; its tests provide position, permission
denial and timeout callbacks and check recovery, one watcher and cleanup. Idle
tests advance a manual clock and dispatch activity events. They check the timeout
boundary and removal of all seven listeners and pending timers. The complete
`//:browser_tests` matrix contains all seven DOM suites plus promise, in both
default development mode and `-c opt` production mode.
