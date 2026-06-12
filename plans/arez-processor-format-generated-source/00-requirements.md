# Format Generated Arez Processor Source - Requirements

Status: accepted

## Mission

Add an opt-in `ArezProcessor` option that formats generated Java source with
`palantir-java-format` before writing it through the annotation processor `Filer`.

## Scope Boundaries

- Applies to `arez.processor.ArezProcessor` generated component source only.
- Does not add a matching option to `ArezPersistProcessor`.
- Does not change default generated output.
- Does not patch Proton for this change.
- Does not add public API for processor option names.
- Does not use formatter import cleanup beyond normal source formatting.

## Locked Decisions And Non-Negotiables

- Option name: `arez.format_generated_source`.
- Default: `false`.
- Enabled only by literal `-Aarez.format_generated_source=true`, matching existing boolean option behavior.
- Formatter dependency: `com.palantir.javaformat:palantir-java-format:2.92.0`.
- SPI dependency: `com.palantir.javaformat:palantir-java-format-spi:2.92.0`.
- Shade and relocate the full non-optional formatter runtime closure into the processor jar.
- Preserve JavaPoet `Filer` behavior, including originating elements and cleanup on write failure.
- Cache the formatter per `ArezProcessor` instance, not statically.
- Formatting failures must fail compilation with a clear processor error.
- Tests must compare both existing unformatted fixtures and formatted fixtures.
- Successful fixture tests must use an Arez-local helper; inherited Proton `assertSuccessfulCompile(...)` overloads are final and cannot be intercepted.

## Command Surface And Behavior Expectations

- Existing behavior:
  - No option or `-Aarez.format_generated_source=false` writes existing JavaPoet output.
- New opt-in behavior:
  - `-Aarez.format_generated_source=true` writes formatted Java source.
  - Formatting uses `Formatter.create().formatSource(String)`.
  - Formatting does not call `formatSourceAndFixImports(String)`.
- JDK 16+ behavior:
  - Users enabling formatting must provide required `jdk.compiler` module exports to the JVM running javac.
  - If exports are missing, compilation fails with an error that identifies the option and explains the module-export requirement.
  - Required export packages:
    - `jdk.compiler/com.sun.tools.javac.api`
    - `jdk.compiler/com.sun.tools.javac.code`
    - `jdk.compiler/com.sun.tools.javac.file`
    - `jdk.compiler/com.sun.tools.javac.parser`
    - `jdk.compiler/com.sun.tools.javac.tree`
    - `jdk.compiler/com.sun.tools.javac.util`

## Formatter Dependency Closure

Add and shade these non-optional formatter runtime artifacts:

- `com.palantir.javaformat:palantir-java-format:2.92.0`
- `com.palantir.javaformat:palantir-java-format-spi:2.92.0`
- `com.google.guava:guava:33.6.0-jre`
- `com.google.guava:failureaccess:1.0.3`
- `com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava`
- `org.jspecify:jspecify:1.0.0`
- `com.google.errorprone:error_prone_annotations:2.49.0`
- `com.google.j2objc:j2objc-annotations:3.1`
- `org.functionaljava:functionaljava:5.0`
- `com.fasterxml.jackson.core:jackson-core:2.21.1`
- `com.fasterxml.jackson.core:jackson-databind:2.21.1`
- `com.fasterxml.jackson.core:jackson-annotations:2.21`
- `com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.21.1`
- `com.fasterxml.jackson.datatype:jackson-datatype-guava:2.21.1`
- `com.fasterxml.jackson.module:jackson-module-parameter-names:2.21.1`

Do not add or shade optional `com.palantir.javaformat:palantir-java-format-parent`.

Do not merge or broad-relocate `com.google.code.findbugs:jsr305:3.0.2`. It is a declared non-optional runtime
dependency but is annotation-only, `Formatter.create().formatSource(String)` was verified to run without it, and
prefix-relocating `javax.annotation` would also catch `javax.annotation.processing.*` references used by the
annotation processor APIs. The packaged-jar checks must verify that no `arez/processor/vendor/javax/annotation/processing`
classes exist and the isolated formatted smoke must run without unshaded formatter dependencies on the classpath.

Relocate formatter dependency packages under `arez.processor.vendor`, including:

- `com.palantir.javaformat`
- `com.google.common`
- `com.google.thirdparty`
- `com.google.errorprone`
- `com.google.j2objc`
- `org.jspecify`
- `fj`
- `com.fasterxml.jackson`

## Quality, Test, And Coverage Gates

- Targeted implementation checks:
  - Compile `arez:processor` tests after dependency and code changes.
  - Package `arez:processor` and inspect the jar for relocated formatter classes.
  - Run an isolated `javac` smoke with only the packaged processor jar on `-processorpath`.
  - Regenerate `processor/src/test/fixtures/expectedFormatted`.
  - Run `bundle exec buildr arez:processor:test`.
  - Run a no-export subprocess compile with `-Aarez.format_generated_source=true` and assert the diagnostic mentions `arez.format_generated_source` and the required exports.
- Required full gate before final completion:
  - `bundle exec buildr ci J2CL=no`
- If full CI is impractical due time or environment, record the exact failure/blocker and at minimum run the focused processor test target.

## Known Intentional Divergences

- Formatted fixtures intentionally differ from existing `fixtures/expected` output.
- Persist processor fixtures are intentionally unchanged.
- The new option intentionally fails rather than falling back to unformatted output if formatting is requested and cannot run.
- `jsr305` is intentionally excluded from the shaded processor jar after review because it is annotation-only, not required
  by formatter execution, and broad `javax.annotation` relocation is unsafe.

## Evidence Collected During Planning

- Proton `AbstractStandardProcessor.emitTypeSpec(...)` is `final`.
- Proton fixture `assertSuccessfulCompile(...)` helpers are `final` and compare against `fixtures/expected`.
- Arez has one production call site for `emitTypeSpec(...)`.
- JavaPoet 0.14.0 `JavaFile.writeTo(Filer)` preserves `typeSpec.originatingElements()` and deletes a partially created file on write failure.
- Maven Central metadata lists `palantir-java-format` latest/release as `2.92.0`.
- `palantir-java-format` 2.92.0 is Java 11 bytecode and works on the repo's Java 17 baseline when required module exports are present.
- In-process formatting on JDK 17 fails without `jdk.compiler` exports with `IllegalAccessError`.
- `jdeps` over `palantir-java-format` 2.92.0 references `com.sun.tools.javac.api`, `code`, `file`, `parser`, `tree`, and `util`.
- The `palantir-java-format-spi` 2.92.0 POM declares `jackson-datatype-guava:2.21.1`.
- Local formatter smoke succeeds without `jsr305` on the classpath when the required JDK exports are present.

## Open Questions Register

### Q-01: Is requiring JDK module exports acceptable for the opt-in formatter?

- status: resolved
- question: Should `arez.format_generated_source=true` require JDK module exports on JDK 16+?
- context: `palantir-java-format` uses `jdk.compiler` internals and fails in-process on JDK 17 without exports.
- options:
  - Accept the requirement and document it.
  - Avoid in-process formatting and seek another integration model.
- tradeoffs: Accepting keeps implementation direct and opt-in; avoiding exports would require materially different process/classloader behavior.
- recommended_default: Accept the requirement because the option defaults to false.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-02: Patch Proton or keep this local to Arez?

- status: resolved
- question: Should this feature require Proton changes?
- context: Proton methods are final, but Arez has one generation call site and can add local helpers.
- options:
  - Keep local to Arez.
  - Patch and release Proton first.
- tradeoffs: Local avoids cross-repo release work; Proton would help only if multiple processors need the same feature.
- recommended_default: Keep local to Arez.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-03: Which formatter version should be pinned?

- status: resolved
- question: Which `palantir-java-format` version should Arez use?
- context: Maven Central reports `2.92.0` as latest/release and it is compatible with Java 17.
- options:
  - Pin `2.92.0`.
  - Use an older known version.
  - Use a dynamic latest version.
- tradeoffs: Pinning latest is reproducible and current; dynamic versions make fixture output non-reproducible.
- recommended_default: Pin `2.92.0` for both main and SPI artifacts.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-04: What dependency closure should be shaded?

- status: resolved
- question: Should Arez shade only direct formatter dependencies or the full runtime closure?
- context: Formatter execution uses the declared runtime closure, including Guava, Jackson, FunctionalJava, and support jars.
- options:
  - Shade full non-optional runtime closure.
  - Shade only direct jars.
- tradeoffs: Full closure avoids processorpath conflicts; direct-only risks runtime linkage conflicts.
- recommended_default: Shade full non-optional runtime closure.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`
- review_adjustment: Exclude annotation-only `jsr305` from the packaged shaded jar because broad `javax.annotation`
  relocation is unsafe and formatter execution was verified without it.

### Q-05: What should the option be named?

- status: resolved
- question: What annotation processor option name should enable formatting?
- context: Existing options use the `arez.*` prefix and some use underscore names.
- options:
  - `arez.format_generated_source`
  - `arez.format.generated_source`
  - another custom name
- tradeoffs: The recommended name is explicit and follows existing underscore style.
- recommended_default: `arez.format_generated_source`.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-06: Should formatting also remove or fix imports?

- status: resolved
- question: Should Arez call formatter import cleanup APIs?
- context: JavaPoet already emits deterministic imports; import cleanup broadens behavior beyond formatting.
- options:
  - Use `formatSource(String)` only.
  - Use `formatSourceAndFixImports(String)`.
- tradeoffs: Formatting only keeps behavior scoped; import cleanup could mask JavaPoet/generator issues.
- recommended_default: Use `formatSource(String)` only.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-07: What happens when formatting fails?

- status: resolved
- question: Should Arez fall back to unformatted output or fail compilation?
- context: Falling back would make `arez.format_generated_source=true` unreliable.
- options:
  - Fail compilation with a clear error.
  - Fall back to unformatted generated source.
- tradeoffs: Failing is explicit and debuggable; fallback hides misconfiguration.
- recommended_default: Fail compilation.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-08: How broad should formatted fixture coverage be?

- status: resolved
- question: Should formatted fixture checks run for every successful generated-output test?
- context: Arez successful fixture comparisons are centralized in local helpers.
- options:
  - Run formatted pass for every successful fixture comparison.
  - Test only a representative subset.
- tradeoffs: Full coverage catches formatter issues across all generated shapes; subset is faster but weaker.
- recommended_default: Run formatted pass for every successful fixture comparison.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-09: How should formatted fixtures be maintained?

- status: resolved
- question: Should fixture regeneration write both `expected` and `expectedFormatted`?
- context: Existing Proton helper writes only `expected`; Arez needs local fixture output logic.
- options:
  - Extend Arez local helper to write both directories.
  - Manually maintain `expectedFormatted`.
- tradeoffs: Automated regeneration avoids manual drift.
- recommended_default: Write both directories when `arez.output_fixture_data=true`.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-10: Should persist processor tests change?

- status: resolved
- question: Should `ArezPersistProcessorTest` adopt formatted fixture checks now?
- context: Persist tests filter out Arez generated outputs and compare persist sidecars.
- options:
  - Leave persist tests unchanged.
  - Add formatted Arez output checks there too.
- tradeoffs: Leaving unchanged keeps scope tight; adding checks duplicates Arez processor coverage.
- recommended_default: Leave persist tests unchanged.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-11: Should docs and changelog be updated?

- status: resolved
- question: Is this user-visible enough to document?
- context: Repo guidelines require changelog updates for annotation processor generation changes.
- options:
  - Update changelog and docs.
  - Update changelog only.
- tradeoffs: Docs help users configure the opt-in module exports; changelog records release impact.
- recommended_default: Update both.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-12: Should formatted emission preserve JavaPoet `Filer` behavior?

- status: resolved
- question: Should Arez manually preserve originating elements and cleanup behavior in the formatted path?
- context: Formatting requires string generation before writing, bypassing direct `JavaFile.writeTo(Filer)`.
- options:
  - Reproduce JavaPoet's Filer write behavior.
  - Write with a simpler custom path.
- tradeoffs: Preserving behavior avoids losing incremental processing metadata or leaving partial files.
- recommended_default: Reproduce JavaPoet's Filer behavior.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-13: Where should the formatter instance live?

- status: resolved
- question: Should Arez cache the formatter instance?
- context: Processor tests run many compilation tasks and generated types.
- options:
  - Lazy per-processor instance field.
  - Static singleton.
  - Create per generated file.
- tradeoffs: Per-processor cache avoids repeated setup without crossing processor/classloader boundaries.
- recommended_default: Lazy per-processor instance field.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-14: Where should test JVM module exports be configured?

- status: resolved
- question: Where should `--add-exports` JVM args be added?
- context: `ArezProcessorTest` invokes the compiler inside the TestNG JVM.
- options:
  - Add exports only to `arez:processor` test JVM.
  - Add exports globally.
  - Add exports as javac options.
- tradeoffs: Processor-test JVM args are narrow and effective; javac options would not affect the hosting JVM.
- recommended_default: Add exports only to `arez:processor` test target.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### Q-15: Should option names become public API constants?

- status: resolved
- question: Should Arez expose a public constant for `format_generated_source`?
- context: Existing processor options are string literals/internal.
- options:
  - Keep internal/package-private.
  - Add public API.
- tradeoffs: Internal keeps API surface narrow; public API is unnecessary for a compiler flag.
- recommended_default: Keep internal.
- user_decision: accept recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`
