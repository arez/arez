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
scenarios stay together in `scenarios_goldens`; Persist also runs the Arez
processor and excludes its outputs when comparing Persist goldens.

Choose a family to run or update:

| Processor | Families | Target prefix |
| --- | --- | --- |
| Arez | component, action, observable, memoize, observe, lifecycle, relationships, metadata, interop, scenarios | `//processor/src/test/java/arez/processor:` |
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
