# How should annotation-processor tests be represented in Bazel?

The current test-class restructuring should preserve a clean path to cacheable Bazel tests without choosing the final
rule, macro, target granularity, or test-runner design now.

## Why it matters

The Bazel shape will determine cache invalidation boundaries and the cost of uncached test execution. One target per
logical fixture would allow a fixture or expected diagnostic change to invalidate only that case, but hundreds of
separate test actions may add analysis, scheduling, sandbox, and JVM-startup overhead to a clean run.

The representation of expectations also affects those boundaries. Diagnostic messages declared as BUILD attributes
can be part of an individual target's action key without recompiling a shared Java data provider. Generated Java
outputs are larger and already suit file-based goldens. The eventual design must also account for formatted and
unformatted output modes, multi-source and multi-output cases, warning promotion, two-phase compilation scenarios,
golden regeneration, and the separate non-hermetic forked-`javac` checks.

## Why deferred

The repository has no checked-in Bazel workspace, Java test rules, or processor-test macro. Its existing
[Bazel task](../../tasks/bazel_j2cl_test.rake) creates a temporary workspace for J2CL and does not establish conventions
for JVM annotation-processor tests. The current work only separates the TestNG suites by execution contract, and the
cost of per-case Bazel targets cannot yet be measured against a representative runner and toolchain.

## Reconsider when

- Before adding the first checked-in Bazel rule, macro, or target for annotation-processor tests.
- After the Bazel Java toolchain and test-runner isolation model are selected.
- When representative clean-cache and warm-cache benchmarks can compare one target per logical fixture with grouped
  targets.

## Context

The current direction separates each processor suite into generated-output, diagnostic, and forked-`javac` test
classes. The Arez processor suite now has separate
[generated-output](../../processor/src/test/java/arez/processor/ArezProcessorGeneratedOutputTest.java),
[diagnostic](../../processor/src/test/java/arez/processor/ArezProcessorDiagnosticTest.java), and
[forked-javac](../../processor/src/test/java/arez/processor/ArezProcessorJavacTest.java) classes. The Persist processor
suite has the corresponding [generated-output](../../persist/processor/src/test/java/arez/persist/processor/ArezPersistProcessorGeneratedOutputTest.java),
[diagnostic](../../persist/processor/src/test/java/arez/persist/processor/ArezPersistProcessorDiagnosticTest.java), and
[forked-javac](../../persist/processor/src/test/java/arez/persist/processor/ArezPersistProcessorJavacTest.java) classes.
The cases remain provider-driven until a Bazel migration is undertaken.

A candidate future shape is one Bazel target per logical fixture, backed by shared runners:

- Generated-output targets declare their source inputs and formatted and unformatted golden outputs.
- Diagnostic targets declare expected success or failure, compiler options, and expected diagnostic kinds and messages
  as BUILD data rather than Java provider data or separate diagnostic golden files.
- A warning case keeps its warning and warnings-as-errors passes in one logical target.
- The unresolved-dependency case remains one two-phase scenario.
- Forked-`javac` toolchain checks use a separate rule or runner because their process and environment contract differs
  from in-process compilation.

This candidate favors incremental cache precision. It is not adopted until target-count overhead, Bazel test-result
caching, parallelism, runner startup cost, diagnostic normalization, and golden-update ergonomics are evaluated.
