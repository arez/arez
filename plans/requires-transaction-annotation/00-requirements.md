# RequiresTransaction Annotation Requirements

## Mission

- Add a new Arez method annotation, `@RequiresTransaction`, that asserts a method is invoked within an active Arez transaction and can optionally constrain the transaction mode and tracking state.

## Scope Boundaries

- In scope: new public annotation and enums, processor parsing/validation, generated runtime assertions, processor fixtures, runtime/integration coverage, API diff metadata, changelog, docs, doc-examples, and structured workflow artifacts.
- Out of scope: changing `@Action`, `@Observe`, or `@Memoize` semantics; introducing new transaction bookkeeping beyond what the runtime already tracks; distinguishing computable transactions from other tracking transactions in the public API.

## Locked Decisions

- The annotation name is `@RequiresTransaction`.
- The annotation is a pure caller-side precondition and MUST NOT create, wrap, or reuse a transaction on behalf of the caller.
- The annotation API uses dedicated enums:
  - `TransactionMode` with `ANY`, `READ_ONLY`, `READ_WRITE`
  - `TrackingMode` with `ANY`, `TRACKING`, `NON_TRACKING`
- The annotation surface is:
  ```java
  @RequiresTransaction(
    mode = TransactionMode.ANY,
    tracking = TrackingMode.ANY
  )
  ```
- `@RequiresTransaction` is mutually exclusive with all other Arez method annotations, including `@Action`.
- `@RequiresTransaction` counts as a processor-recognized component element for `@ArezComponent` non-empty validation, so a component containing only `@RequiresTransaction` methods is valid.
- `@RequiresTransaction` does not count when deciding whether `@ArezComponent(allowEmpty = true)` is unnecessary; `allowEmpty = true` on a component containing only `@RequiresTransaction` methods should not trigger the unnecessary-`allowEmpty` warning.
- Valid targets are methods in `@ArezComponent` or `@ArezComponentLike` types that are non-private, non-static, non-final, non-abstract, and accessible to the generated implementation.
- Runtime assertions are invariant-gated, aligned with existing Arez runtime checks, rather than unconditional production checks.
- `TransactionMode` constraints degrade to presence-only checks when transaction-type enforcement is disabled globally.
- V1 tracking semantics distinguish only `TRACKING` vs `NON_TRACKING`; computable transactions remain an internal runtime detail.
- Runtime diagnostics should distinguish:
  - no active transaction,
  - wrong transaction mode,
  - wrong tracking state.
- Broader docs land in a dedicated annotation page plus a cross-link from `transactions.md`.

## Command Surface

- Public API additions:
  - `arez.annotations.RequiresTransaction`
  - `arez.annotations.TransactionMode`
  - `arez.annotations.TrackingMode`
- Processor behavior:
  - reject misplaced or structurally invalid `@RequiresTransaction` usage at compile time,
  - reject methods annotated with both `@RequiresTransaction` and any other Arez method annotation,
  - treat `@RequiresTransaction` as satisfying component non-empty validation,
  - ignore `@RequiresTransaction` when computing whether `allowEmpty = true` is unnecessary,
  - generate overriding methods that perform runtime precondition checks before delegating to user code.
- Runtime behavior:
  - if no transaction is active, fail the invariant check,
  - if `mode != ANY`, validate the current transaction mode when that information is meaningful,
  - if `tracking != ANY`, validate whether the current transaction is tracking,
  - never create a new transaction as part of the check.

## Behavior Expectations

- The generated override should preserve existing component lifecycle/disposal guard ordering before invoking the user-defined method body.
- The generated check should use existing `ArezContext` transaction state helpers wherever possible.
- Error messages should report both expected and actual state when mode or tracking mismatch occurs.
- Runtime failures should allocate Arez-style invariant/API-invariant codes, update the diagnostic message catalog, and be covered by exact-message assertions.
- The feature should behave consistently for concrete methods declared on classes, inherited methods, and interface default methods processed by Arez.
- The feature should prove separate behavior for inherited methods and interface default methods rather than treating them as interchangeable coverage.
- The new public types must be reflected in API compatibility artifacts.

## Quality Gates

- Targeted iteration checks:
  - `bundle exec buildr arez:processor:test`
  - `bundle exec buildr arez:core:test`
  - `bundle exec buildr arez:integration-tests:test`
- Required full gate:
  - `bundle exec buildr test PRODUCT_VERSION=0.244 PREVIOUS_PRODUCT_VERSION=0.243`
- User-visible behavior requires updates to:
  - `CHANGELOG.md`
  - public Javadocs
  - documentation and doc-examples
  - API diff metadata

## Known Intentional Divergences

- `@RequiresTransaction(mode = READ_ONLY|READ_WRITE)` does not introduce new transaction-mode bookkeeping when `Arez.shouldEnforceTransactionType()` is false; mode checks intentionally degrade to transaction-presence checks in that configuration.
- V1 does not expose a computable-specific tracking mode even though the runtime can detect computable transactions internally.

## Open Questions Register

| id | status | question | context | options | tradeoffs | recommended_default | user_decision | artifacts_updated |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Q-01 | resolved | Which annotation name should be standardized on? | The public name drives Javadocs, diagnostics, and mental model. | `@RequiresTransaction`; `@ExpectTransaction`; `@TransactionRequired` | `@RequiresTransaction` reads like a precondition and does not imply transaction creation. | `@RequiresTransaction` | `@RequiresTransaction` | `00-requirements.md`, `01-feature-requires-transaction.md`, `10-implementation-plan.md`, `20-task-board.yaml`, `40-test-strategy.md` |
| Q-02 | resolved | What should v1 verify? | The annotation can assert only presence or also transaction characteristics. | Presence only; presence plus mode; presence plus mode and tracking | Supporting mode and tracking in v1 avoids a follow-up API break or immediate expansion. | Presence plus mode and tracking | Presence plus mode and tracking | same |
| Q-03 | resolved | How fine-grained should tracking validation be? | The runtime can distinguish computable transactions, but exposing that changes the public API shape. | `ANY`/`TRACKING`/`NON_TRACKING`; include computable state; no tracking attribute | The 3-state model covers the intended use without overfitting v1. | 3-state tracking only | 3-state tracking only | same |
| Q-04 | resolved | Can `@RequiresTransaction` mix with other Arez method annotations? | Mixing with `@Action` or `@Observe` creates ambiguous semantics. | Disallow all mixing; allow with `@Action` only; allow broader mixing | Mutual exclusivity keeps the contract crisp and diagnostics simpler. | Disallow all mixing | Disallow all mixing | same |
| Q-05 | resolved | How strong should runtime enforcement be? | The feature can be compile-time only, always-on, or aligned with existing invariant checks. | Invariant-gated runtime check; always enforce at runtime; compile-time only | Aligning with current Arez invariant policy avoids surprising production overhead. | Invariant-gated runtime check | Invariant-gated runtime check | same |
| Q-06 | resolved | What attribute shape should the annotation use? | The API can mirror existing `mutation`, use tri-state booleans, or use explicit current-state nouns. | `mode` + `tracking`; `readOnly` + `tracking`; `mutation` + `tracking` | `mode` expresses the required current transaction state more clearly than `mutation`. | `mode` + `tracking` | `mode` + `tracking` | same |
| Q-07 | resolved | Should the attributes use dedicated enums? | Reusing generic enums would avoid new types but weaken the public contract. | Dedicated enums; reuse generic enums; nested enums inside the annotation | Dedicated enums make diagnostics and docs much clearer. | Dedicated enums | Dedicated enums | same |
| Q-08 | resolved | How should mode checks behave when transaction-type enforcement is disabled? | Read-only vs read-write is not meaningfully tracked in that configuration today. | Degrade to presence-only; add extra bookkeeping; reject `mode` usage | Reusing existing runtime state avoids hidden complexity. | Degrade to presence-only | Degrade to presence-only | same |
| Q-09 | resolved | What should the unconstrained enum value be called? | The sentinel value appears in public annotation usage and diagnostics. | `ANY`; `DONT_CARE`; `DEFAULT` | `ANY` is concise and reads naturally in annotations. | `ANY` | `ANY` | same |
| Q-10 | resolved | Where should broader docs land? | The feature needs discoverable docs beyond Javadocs. | Dedicated annotation page + transaction cross-link; transactions doc only; action doc only | A dedicated annotation page fits the established doc structure for method annotations. | Dedicated annotation page + transaction cross-link | Dedicated annotation page + transaction cross-link | same |
| Q-11 | resolved | How should `@RequiresTransaction` interact with component non-empty and `allowEmpty` validation? | The processor currently rejects components with no recognized reactive elements and warns when `allowEmpty = true` is unnecessary. | Count it for both checks; count it only for non-empty validation; ignore it for both checks. | Counting it for non-empty validation avoids artificial compilation failures, while ignoring it for unnecessary-`allowEmpty` warnings keeps `allowEmpty = true` usable for components that only declare transaction-precondition helpers. | Count it only for non-empty validation | Count it only for non-empty validation | `00-requirements.md`, `01-feature-requires-transaction.md`, `10-implementation-plan.md`, `20-task-board.yaml`, `40-test-strategy.md` |
