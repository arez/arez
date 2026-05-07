# Implementation Plan

## Phase Sequence

1. Finalize and maintain structured-delivery artifacts under `plans/requires-transaction-annotation/`.
2. Add the public API surface for `@RequiresTransaction`, `TransactionMode`, and `TrackingMode`, including Javadocs, API diff metadata, and changelog.
3. Extend the processor to discover, parse, and validate `@RequiresTransaction` targets using a dedicated descriptor, existing container/overridability checks, and component-level recognized-element lists, with separate handling for non-empty validation vs unnecessary-`allowEmpty` warnings.
4. Generate runtime precondition wrappers and diagnostic-catalog updates that assert active transaction, transaction mode, and tracking state before delegating to the user implementation.
5. Add processor fixture coverage for valid targets, invalid targets, mixed-annotation failures, and generated-code expectations.
6. Add runtime and integration coverage for success and failure cases across transaction presence, mode, tracking, inheritance/default methods, and transaction-type-enforcement-disabled behavior.
7. Add a dedicated annotation doc page, transaction cross-links, sidebar/title updates, and doc-examples.
8. Run verification gates, update task evidence, and close out the task board.

## Delivery Approach

- Keep the public API small and explicit: one annotation plus two dedicated enums.
- Follow the existing processor pattern by introducing a dedicated descriptor instead of overloading `ActionDescriptor`.
- Reuse existing `ArezContext` transaction-state helpers in generated code rather than adding new public runtime APIs unless implementation proves that impossible.
- Use a single shared processor notion of "Arez method annotations that conflict with `@RequiresTransaction`" and test that list exhaustively.
- Separate structural validation from runtime behavior:
  - processor owns shape, placement, accessibility, and mutual-exclusion rules,
  - generated code owns invocation-time transaction assertions.
- Keep docs aligned with the feature intent: `@Action` creates or reuses transactional boundaries; `@RequiresTransaction` asserts that one is already active.

## Task Granularity Rules

- Each implementation task must include the associated tests/docs/process updates needed for that slice.
- No task should require simultaneous edits across API, processor, docs, and integration tests unless the slice is impossible to validate independently.
- Full-gate execution stays isolated in the final close-out task.

## High-Risk Areas

- Risk 1: generated checks may duplicate or conflict with existing lifecycle/disposal guards.
  - Impact: user methods could fail with confusing diagnostics or in the wrong order.
  - Mitigation: insert `@RequiresTransaction` assertions only after existing initialization/construction/disposal guards and prove the ordering via expected generated-source fixtures.
- Risk 2: transaction-mode validation may behave inconsistently when transaction-type enforcement is disabled.
  - Impact: the public API could imply stronger guarantees than the runtime can actually honor.
  - Mitigation: document the degradation explicitly and add dedicated runtime coverage for that configuration.
- Risk 3: mutual-exclusion validation may drift from the current Arez method-annotation set.
  - Impact: invalid combinations could compile silently or valid combinations could be rejected accidentally.
  - Mitigation: centralize the new annotation alongside the existing Arez method-annotation detection paths and add exhaustive processor fixtures for every conflicting method annotation from the shared detector/list.
- Risk 4: `@RequiresTransaction` may be added to method parsing but handled inconsistently across the component non-empty and unnecessary-`allowEmpty` warning paths.
  - Impact: a component containing only `@RequiresTransaction` methods could either fail compilation unexpectedly or emit the wrong warning behavior.
  - Mitigation: update the recognized-element logic explicitly so `@RequiresTransaction` counts for non-empty validation but is ignored for unnecessary-`allowEmpty` warnings, and add dedicated fixtures for both paths.
- Risk 5: public docs may blur the distinction between `@Action` and `@RequiresTransaction`.
  - Impact: downstream users may annotate transaction boundaries incorrectly.
  - Mitigation: add side-by-side guidance in Javadocs and docs that says when each annotation should be used.
- Risk 6: API compatibility metadata may be forgotten because the change spans code generation and runtime behavior as well as public API.
  - Impact: release tooling may miss the public API addition.
  - Mitigation: include the API diff file in the API task acceptance criteria and required file list.

## Required Full Gates

- `bundle exec buildr test PRODUCT_VERSION=0.244 PREVIOUS_PRODUCT_VERSION=0.243`

## Decision Log

| question | concrete plan change |
| --- | --- |
| Q-01 | Use `RequiresTransaction` consistently across code, docs, diagnostics, task names, and generated examples. |
| Q-02 | Plan includes both `TransactionMode` and `TrackingMode` from the initial API task onward. |
| Q-03 | Runtime coverage and docs stop at `TRACKING` vs `NON_TRACKING`; no computable-specific public API is planned. |
| Q-04 | Processor task includes explicit mixed-annotation rejection rather than coexistence rules. |
| Q-05 | Generated code task targets invariant-gated checks and does not plan always-on production enforcement. |
| Q-06 | Public annotation API uses `mode` and `tracking`, which drives Javadocs, examples, and diagnostics. |
| Q-07 | API task adds top-level enum types in `arez.annotations` and includes API diff metadata updates. |
| Q-08 | Runtime/integration test task includes a dedicated scenario for transaction-type-enforcement-disabled behavior. |
| Q-09 | All API/docs/examples use `ANY` as the unconstrained value. |
| Q-10 | Docs task includes `docs/at_requires_transaction.md`, `docs/transactions.md`, sidebar updates, title localization, and doc-examples. |
| Q-11 | Processor validation and fixtures must treat `@RequiresTransaction` as a recognized component element for non-empty validation while excluding it from unnecessary-`allowEmpty` warnings. |

## Completion Criteria

- `@RequiresTransaction` and its enums are present in the public API and reflected in API diff metadata.
- A component containing only `@RequiresTransaction` methods satisfies the component non-empty validation.
- `@ArezComponent(allowEmpty = true)` with only `@RequiresTransaction` methods does not trigger the unnecessary-`allowEmpty` warning.
- Invalid usage is rejected at compile time with clear diagnostics.
- Generated code asserts transaction presence and optional mode/tracking requirements before delegating to user code.
- Diagnostic catalog entries and exact-message assertions are updated for all new runtime failures.
- Processor fixtures prove shape validation, non-empty component handling, exhaustive mutual-exclusion coverage, and generated output.
- Runtime/integration tests cover all supported constraint combinations, the disabled-transaction-type edge case, separate inherited and interface-default paths, and the absence of new spy events.
- Docs, doc-examples, and changelog describe the feature and its distinction from `@Action`.
- The task board contains evidence for all completed tasks and no completed task retains `commit.hash: pending`.
