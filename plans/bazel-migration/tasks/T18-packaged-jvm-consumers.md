# T18 — Expose and test packaged JVM consumer targets

- Status: pending
- Blocked by: T17
- Spec coverage: R02, R03, R08, AC02, AC03, AC08

## Delivers

Downstream JVM users import the actual release jars and run packaged Arez/Persist processors.

## Acceptance criteria

- [ ] Expose root public core, processor, dom, promise, spytools, testng, persist_core and persist_processor imports with correct dependency exports and source jars.
- [ ] Expose public arez_processor_plugin and arez_persist_processor_plugin using packaged shaded processors, not internal source plugins.
- [ ] Add a tiny isolated compile/run consumer of each relevant runtime and both processors, reading an inherited compiled JSpecify contract.
- [ ] Use Java 21 and isolated processorpaths without hidden Proton/JavaPoet/JSpecify runtime jars; consumer compilation still declares JSpecify. This execution is mandatory evidence that the separately checked shaded Java 21 dependencies remain usable on the documented runtime.
- [ ] Document consumer labels/module overrides and processor JDK/export requirements. Keep external downstream-project builds out.

## Validation

- Build all eight public imports and both public processor plugins.
- python3 tools/release/check_artifacts.py — runs isolated packaged compile/run checks in addition to archive inspection.
- Inspect consumer compilerpaths/generated output — proves actual release artifacts and compiled type-use contracts are exercised.

## Evidence

- pending
