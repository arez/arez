# Format Generated Arez Processor Source - Implementation Plan

Status: accepted

## Delivery Approach

Implement the feature locally in Arez with a small production path change, explicit shaded dependencies, and full fixture coverage for formatted output. The plan remains in review until user approval is recorded.

## Ordered Phase Sequence

### Phase 1: Build And Dependency Wiring

1. Add formatter artifacts to `build.yaml`.
2. Add every artifact listed in `00-requirements.md` under "Formatter Dependency Closure".
3. Add formatter artifacts and all required non-optional runtime transitive dependencies to `arez:processor` compile/runtime packaging.
4. Extend processor jar shading/relocation rules for the exact packages listed in the requirements.
5. Exclude `jsr305` from the packaged shaded processor jar; do not broad-relocate `javax.annotation`.
6. Add `--add-exports` JVM args only to `arez:processor` test target and the processor IPR TestNG configuration.
7. Verify dependency resolution with `bundle exec buildr arez:processor:compile`.
8. Package the processor and inspect the jar for relocated formatter classes, `jackson-datatype-guava`, and absence of `arez/processor/vendor/javax/annotation/processing`.

### Phase 2: Processor Option And Emission

1. Add internal option constant `format_generated_source`.
2. Register `arez.format_generated_source` in `@SupportedOptions`.
3. Add lazy per-instance formatter cache.
4. Replace the single production `emitTypeSpec(...)` call with an Arez-local emission helper.
5. Build `JavaFile` with `skipJavaLangImports(true)` in both branches.
6. For unformatted output, preserve current JavaPoet write behavior.
7. For formatted output, call `Formatter.create().formatSource(javaFile.toString())`, then write through `Filer.createSourceFile(...)` with originating elements.
8. Convert formatter and linkage/access failures to clear `IOException` messages that mention `arez.format_generated_source` and JDK module exports where relevant.
9. Mirror JavaPoet 0.14.0 `JavaFile.writeTo(Filer)` semantics:
   - filename is `packageName + "." + typeSpec.name()` or `typeSpec.name()` for the default package.
   - `typeSpec.originatingElements()` are passed to `Filer.createSourceFile(...)`.
   - writer is closed via try-with-resources.
   - partially created source file is deleted if writing fails.

### Phase 3: Test Harness And Fixtures

1. Introduce a new non-conflicting Arez-local successful fixture helper; do not rely on final Proton `assertSuccessfulCompile(...)` overloads.
2. Update every successful fixture-comparison call site in `ArezProcessorTest` to use the Arez-local helper.
3. Implement the dual-run helper:
   - pass 1: default options, compare/write `fixtures/expected`.
   - pass 2: options plus `-Aarez.format_generated_source=true`, compare/write `fixtures/expectedFormatted`.
4. Keep failure/warning diagnostic tests unchanged unless they use successful fixture comparison helpers.
5. Add `expectedFormatted` as an IDE test source directory if needed.
6. Regenerate formatted fixtures with `arez.output_fixture_data=true`.
7. Add an isolated no-export subprocess compile that enables formatting and asserts the diagnostic mentions `arez.format_generated_source` and the required JDK exports.
8. Run `bundle exec buildr arez:processor:test`.

### Phase 4: Documentation And Changelog

1. Add an Unreleased changelog entry for the new processor option.
2. Update `docs/project_setup.md` with:
   - option name and default.
   - exact module export flags for JDK 16+ when enabled.
   - guidance that exports must reach the JVM running javac/annotation processing, not just ordinary annotation processor options.
   - concrete examples for `.mvn/jvm.config` or `MAVEN_OPTS`, and forked javac `-J--add-exports` usage where applicable.
   - note that dependencies are shaded inside the processor artifact.
3. Keep docs concise and avoid broader setup rewrites.

### Phase 5: Validation And Cleanup

1. Inspect `git diff` for unintended fixture or generated artifact churn.
2. Run focused processor tests again after fixture regeneration.
3. Run `bundle exec buildr arez:processor:package`.
4. Inspect the packaged jar for relocated formatter classes.
5. Run an isolated `javac` smoke using only the packaged processor jar on `-processorpath`, with `-Aarez.format_generated_source=true` and required JDK exports.
6. Verify the smoke generated formatted source and did not load unshaded formatter dependencies from the classpath.
7. Run full gate: `bundle exec buildr ci J2CL=no`.
8. Record command evidence in `20-task-board.yaml`.
9. Prepare commits aligned to completed tasks if requested/approved.

## High-Risk Areas And Mitigations

- Formatter module access on JDK 16+:
  - Mitigation: narrow test JVM exports; clear compile error for users.
- Shading an incomplete dependency closure:
  - Mitigation: include exact full non-optional runtime closure and verify packaged jar contents plus isolated processorpath smoke.
- Unsafe `javax.annotation` relocation:
  - Mitigation: exclude annotation-only `jsr305`, do not broad-relocate `javax.annotation`, and add negative jar checks.
- Losing JavaPoet originating elements:
  - Mitigation: reproduce JavaPoet Filer write path using `typeSpec.originatingElements()` and add a parity checklist/test.
- Fixture churn:
  - Mitigation: keep existing `expected` unchanged and isolate formatted output in `expectedFormatted`.
- Test time increase:
  - Mitigation: only dual-run successful fixture comparisons, not warning/failure tests.

## Required Full Gate

```bash
bundle exec buildr ci J2CL=no
```

## Targeted Gates

```bash
bundle exec buildr arez:processor:compile
bundle exec buildr arez:processor:package
bundle exec buildr arez:processor:test
```

## Decision Log

- Q-01: Formatting may require JDK module exports when enabled; default false protects existing users.
- Q-02: Implementation stays local to Arez; no Proton patch.
- Q-03: Pin formatter and SPI at `2.92.0`.
- Q-04: Shade full non-optional formatter runtime closure.
- Q-05: Use option name `arez.format_generated_source`.
- Q-06: Use `formatSource(String)` only, not import cleanup.
- Q-07: Fail compilation clearly on formatter failure.
- Q-08: Run formatted fixture pass for every Arez successful fixture comparison.
- Q-09: Regeneration writes both `expected` and `expectedFormatted`.
- Q-10: Persist processor tests remain unchanged.
- Q-11: Update changelog and `docs/project_setup.md`.
- Q-12: Preserve JavaPoet Filer behavior manually in formatted branch.
- Q-13: Cache formatter per `ArezProcessor` instance.
- Q-14: Add module exports only to `arez:processor` test JVM.
- Q-15: Keep option name internal, not public API.

## Review Loop History

- Round 1 plan review findings accepted:
  - final Proton fixture helpers cannot intercept most successful compile call sites.
  - formatter dependency closure and relocation list needed exact artifacts.
  - packaged processor jar needed self-contained validation.
  - module export docs/tests needed exact flags and placement.
  - formatter failure diagnostic needed no-export subprocess coverage.
  - formatted Filer path needed an explicit JavaPoet parity checklist.
- Round 2 plan review findings accepted:
  - added missing `jackson-datatype-guava` SPI dependency.
  - removed unsafe broad `javax.annotation` relocation and excluded annotation-only `jsr305`.
  - tightened packaged-jar smoke to enable formatted emission with required exports and verify formatted output.

## Review Gate

Accepted for implementation by user instruction on 2026-06-13.
