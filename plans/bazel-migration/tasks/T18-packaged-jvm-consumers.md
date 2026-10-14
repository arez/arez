# T18 — Expose and test packaged JVM consumer targets

- Status: complete
- Blocked by: T17
- Spec coverage: R02, R03, R08, AC02, AC03, AC08

## Delivers

Downstream JVM users import the actual release jars and run packaged Arez/Persist processors.

## Acceptance criteria

- [x] Expose root public core, processor, dom, promise, spytools, testng, persist_core and persist_processor imports with correct dependency exports and source jars.
- [x] Expose public arez_processor_plugin and arez_persist_processor_plugin using packaged shaded processors, not internal source plugins.
- [x] Add a tiny isolated compile/run consumer of each relevant runtime and both processors, reading an inherited compiled JSpecify contract.
- [x] Use Java 21 and isolated processorpaths without hidden Proton/JavaPoet/JSpecify runtime jars; consumer compilation still declares JSpecify. This execution is mandatory evidence that the separately checked shaded Java 21 dependencies remain usable on the documented runtime.
- [x] Document consumer labels/module overrides and processor JDK/export requirements. Keep external downstream-project builds out.

## Validation

- Build all eight public imports and both public processor plugins.
- python3 tools/release/check_artifacts.py — runs isolated packaged compile/run checks in addition to archive inspection.
- Inspect consumer compilerpaths/generated output — proves actual release artifacts and compiled type-use contracts are exercised.

## Evidence

- Added all eight root public `java_import` labels with assembled binary/source jars and required API dependencies. Both public processor plugins depend only on the corresponding packaged shaded import. Building all ten labels passed; no public target aliases an internal source plugin.
- `python3 tools/release/check_artifacts.py` passed an isolated Java 21 compile/run consumer whose processorpath contains only the assembled Arez/Persist jars. It compiles `Parent<T extends @Nullable Object>` separately, then specializes it as `Parent<@Nullable String>` and asserts generated nullable generic/array/element contracts. Generated classes target Java 17.
- The consumer runs reactive initial/update/null propagation, persisted value storage and null removal, disposal, DOM Position values, SpyUtil nesting and TestNG support context. Promise JVM enum/API availability is checked; native promise/DOM behavior belongs to the declared browser tasks. [Consumer/artifact evidence](../T18-artifact-consumer-evidence.json) records the isolated inputs and unchanged independent hashes for all 32 artifacts and aggregate docs.
- The compiled generic Persist consumer exposed use of the original superclass type variable in setter matching, save guards and converter identity. Resolved those member types once in the concrete component context; retained original declaration elements for annotation/default semantics. Added paired source/compiled generic nullable property goldens in both modes. Existing goldens are unchanged.
- `bazel test //:jvm_tests --keep_going --test_output=errors` passes all 31 targets: [2,301 TestNG invocations](../T18-jvm-results.json), zero failures/skips, including all original cases plus the two new Persist cases. T12 evidence and Unreleased notes record the generic substitution correction.
- [Development documentation](../../../docs/development.md) documents all labels, immutable git/local downstream overrides, Java 17 output/Java 21 processor requirements, type-annotation/parameter-name flags and formatter exports. It does not reinstate downstream project builds.
- Palantir, Buildifier, Python execution, whitespace, plan and domain-documentation link checks passed.
- Knowledge-sweep follow-up: an initial nested consumer exposed a pre-existing Persist sidecar factory-name mismatch. The original generator constructs a nested-prefix name from the model while Arez enhances the emitted top-level sidecar name. The final tiny consumer uses a top-level model; nested Persist support is outside this migration and this finding must be classified in T26 alongside the existing Object restore issue.

## Implementation review correction

- Round 1 reproduced a failing real downstream module graph and a strict-dependency failure on generated Braincheck guards. The earlier isolated javac consumer remains useful but did not exercise these Bazel integration boundaries.
- Public core now exports the mandatory generated-code Braincheck dependency. `tools/bazel_consumer_config.py` copies required root rule pins, patch inputs and the Maven lock from the selected Arez checkout. Development documentation provides the complete include/immutable Arez override procedure and root Java configuration requirements.
- `python3 tools/check_bazel_consumer.py` passed in a separate disposable workspace without system/home rc or local overrides of Proton/Sting. It uses the copied root inputs, all six public runtime imports and both packaged plugins, compiles with strict dependencies, and runs reactive propagation, unmarked null handling, generated Persist sidecar and runtime API checks. The shared full check gate includes this regression.
