# RequiresTransaction Feature Requirements Deep Dive

## Feature

- Name: `@RequiresTransaction`
- Owner: Arez core / processor maintainers
- Related plan/task IDs: `PLAN-APPROVAL`, `T01`, `T02`, `T03`, `T04`, `T05`, `T06`, `T07`

## Problem Statement

- Arez currently provides `@Action` for methods that run within a transaction, but it does not provide a first-class way to mark helper methods that must already be executing inside a transaction.
- Today these helper-method assumptions are implicit, so misuse is easy:
  - callers can invoke a helper outside a transaction and only hit lower-level invariant failures later,
  - method signatures do not communicate whether read-only, read-write, tracking, or non-tracking context is required,
  - downstream framework authors cannot express transaction preconditions declaratively in component APIs.

## Scope

- In scope:
  - a new public precondition annotation,
  - compile-time validation of target legality and exclusivity,
  - generated runtime assertions,
  - focused diagnostics and documentation,
  - coverage across processor and runtime tests.
- Out of scope:
  - auto-wrapping helpers in actions,
  - changing nested-action rules,
  - broad refactors to the transaction runtime,
  - exposing computable-specific transaction requirements in v1.

## Inputs and Interfaces

- New annotation:
  ```java
  @Documented
  @Target( ElementType.METHOD )
  public @interface RequiresTransaction
  {
    @Nonnull
    TransactionMode mode() default TransactionMode.ANY;

    @Nonnull
    TrackingMode tracking() default TrackingMode.ANY;
  }
  ```
- New enums:
  - `TransactionMode.ANY`, `READ_ONLY`, `READ_WRITE`
  - `TrackingMode.ANY`, `TRACKING`, `NON_TRACKING`
- No new runtime end-user API methods are planned; generated code should rely on existing `ArezContext` helpers.

## Behavior Requirements

1. Methods annotated with `@RequiresTransaction` MUST compile only inside `@ArezComponent` or `@ArezComponentLike` types.
2. Annotated methods MUST be non-private, non-static, non-final, non-abstract, and accessible to the generated implementation type.
3. Annotated methods MUST NOT carry any other Arez method annotation.
4. A component containing only `@RequiresTransaction` methods MUST satisfy the non-empty `@ArezComponent` validation.
5. A component containing only `@RequiresTransaction` methods and annotated with `@ArezComponent(allowEmpty = true)` MUST NOT trigger the unnecessary-`allowEmpty` warning.
6. Generated overrides MUST assert that an active transaction exists before delegating to user code.
7. `mode = READ_ONLY` MUST require a read-only transaction when transaction-type enforcement is enabled.
8. `mode = READ_WRITE` MUST require a read-write transaction when transaction-type enforcement is enabled.
9. `tracking = TRACKING` MUST require a tracking transaction.
10. `tracking = NON_TRACKING` MUST require a non-tracking transaction.
11. When transaction-type enforcement is disabled, `mode` constraints MUST degrade to presence-only behavior rather than introducing independent bookkeeping.
12. The annotation MUST NOT create or wrap transactions and MUST NOT emit action or transaction spy events of its own.

## Error Handling and Diagnostics

- Compile-time diagnostics should:
  - identify invalid target shape,
  - identify illegal mixing with other Arez method annotations,
  - identify usage outside valid Arez container types.
- Runtime diagnostics should:
  - allocate new Arez-style invariant or API-invariant codes and update `diagnostic_messages.json`,
  - distinguish no-transaction vs wrong-mode vs wrong-tracking failures,
  - include expected state and actual state when available,
  - use the existing Arez invariant style and numbering scheme.

## Compatibility and Parity

- Baseline behavior source:
  - `@Action` documents transaction creation/reuse semantics,
  - `ArezContext` exposes transaction state helpers,
  - other Arez method annotations already enforce overridable/non-static/non-private rules via the processor.
- Intentional divergences:
  - unlike `@Action`, `@RequiresTransaction` does not start a transaction,
  - unlike `@Action(mutation = ...)`, `@RequiresTransaction(mode = ...)` describes the required current transaction state rather than the transaction the method should create.

## Non-Functional Requirements

- Determinism:
  - generated checks must use stable ordering and produce deterministic diagnostics.
- Performance:
  - runtime checking must be gated the same way existing Arez invariants are gated.
- Maintainability:
  - avoid duplicating transaction-state detection logic when `ArezContext` helpers already exist.
- Documentation:
  - the distinction between `@Action` and `@RequiresTransaction` must be explicit in Javadocs and docs.
- Diagnostics:
  - diagnostic catalog changes must remain synchronized with exact-message tests.

## Acceptance Criteria

- [ ] `arez.annotations.RequiresTransaction` exists with final Javadocs.
- [ ] `TransactionMode` and `TrackingMode` exist as top-level public enums.
- [ ] Invalid targets fail compilation with clear diagnostics.
- [ ] Mixed Arez method annotations on the same method fail compilation.
- [ ] A component containing only `@RequiresTransaction` methods compiles without `allowEmpty`.
- [ ] `@ArezComponent(allowEmpty = true)` with only `@RequiresTransaction` methods does not trigger the unnecessary-`allowEmpty` warning.
- [ ] Generated code performs invariant-gated transaction checks before delegating to user code.
- [ ] Runtime checks distinguish active-transaction, mode, and tracking mismatches.
- [ ] Runtime diagnostics allocate/update message catalog entries and are verified by exact-message assertions.
- [ ] `@RequiresTransaction` emits no action or transaction spy events of its own.
- [ ] API diff metadata reflects the new public API.
- [ ] Docs and doc-examples explain when to use `@RequiresTransaction` instead of `@Action`.

## Validation Plan

- Targeted checks:
  - `bundle exec buildr arez:processor:test`
  - `bundle exec buildr arez:core:test`
  - `bundle exec buildr arez:integration-tests:test`
- Full gates:
  - `bundle exec buildr test PRODUCT_VERSION=0.244 PREVIOUS_PRODUCT_VERSION=0.243`

## Open Questions

- None. All design questions are resolved in `00-requirements.md`.
