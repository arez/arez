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
