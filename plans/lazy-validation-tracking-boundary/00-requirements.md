# Lazy Validation Tracking Boundary Requirements

Status: completed

## Mission

Design a core Arez change that prevents lazy `POSSIBLY_STALE` validation from leaking transitive computed dependencies into the current outer tracker, while preserving existing value semantics for recomputation, equality comparison, exception caching, scheduling, and deactivation.

## Scope Boundaries

- In scope: `ComputableValue`, `Observer`, `Transaction`, `ObservableValue`, `MemoizeCache`, spy graph introspection, and regression tests around graph shape.
- In scope: implementation design and future task breakdown for a human implementer.
- Out of scope for this design phase: implementation code changes, branch creation, commits, and compatibility shims.
- Out of scope for the first implementation unless evidence demands it: changing `WhyRun` formatting or hiding graph edges in spy APIs.

## Evidence From Research

- `ComputableValue.get()` reports the computed observable as observed before checking `Observer.shouldCompute()`.
- `Observer.shouldCompute()` validates `POSSIBLY_STALE` computed dependencies by calling `computableValue.get()`.
- Those recursive validation reads execute while the outer tracker transaction remains active, so `Transaction.observe()` records deeper computed dependencies on the outer tracker.
- `Transaction.completeTracking()` installs recorded observables as actual dependencies and observer backlinks.
- `WhyRun` renders the runtime graph from spy info; it is not inventing the extra observer relationships.
- `MemoizeCache.get()` delegates directly to `ComputableValue.get()`, so memoized accessors follow the same core path.

## Locked Decisions

- Fix the actual runtime dependency graph; do not solve this as a `WhyRun` presentation issue.
- Keep normal source-level `ComputableValue.get()` dependency tracking behavior.
- Suppress only recursive validation observations triggered by `POSSIBLY_STALE` dependency validation.
- Keep the immediate dependency edge visible to the outer tracker.
- Suppressed recursive validation reads must be completely invisible to dependency tracking bookkeeping: no observed-list append, no last-tracker marker update, and no duplicate-elimination state mutation.
- Preserve equality-based no-change propagation as the only downstream stale gate.
- Preserve current exception behavior in `Observer.shouldCompute()`.
- Preserve real recompute tracking under the recomputing computable's own tracker.
- Use a narrow internal, stack-scoped observation suppression mechanism rather than a public compatibility mode.
- Do not add a backward compatibility shim or flag.
- Add comprehensive graph-shape regression tests.

## Command Surface And Behavior Expectations

- `ComputableValue.get()` should keep public ordering: observe first, then decide whether to compute or validate.
- `Observer.shouldCompute()` should use an internal validation-aware path rather than public `get()` directly.
- Recursive validation reads should not append observables to the outer transaction's observed list.
- Recursive validation reads should not update `ObservableValue` last-tracker transaction state or any equivalent "already observed" marker for the active outer tracker.
- If a recursive validation read touches an observable that the outer computation later reads directly, the later direct read must still be recorded normally.
- Actual recomputation should still use the normal computable tracking transaction and refresh dynamic dependencies normally.
- `reportPossiblyChanged()` and `reportChangeConfirmed()` propagation should remain active during validation.
- `READ_OUTSIDE_TRANSACTION` computables should also respect recursive validation suppression when a tracking transaction is active.

## Plan Status Authority

- `plans/lazy-validation-tracking-boundary/20-task-board.yaml` field `plan.status` is the authoritative plan state.
- Markdown `Status:` headers mirror the task board and are not authoritative if they drift.
- Valid design-phase outcomes are `draft`, `accepted`, `deferred`, and `rejected`.
- The plan remains `draft` until the user explicitly approves, defers, or rejects it after review.
- Each completed iterative review round increments `plan.review_rounds_completed` in the task board and records the round outcome in `PLAN-REVIEW` evidence or history.
- `PLAN-REVIEW.status` changes to `completed` only when a no-actionable-finding round occurs or the five-round limit is reached.
- `PLAN-APPROVAL.status` and `plan.approval_outcome` remain pending until the user gives explicit direction after the review loop.

## Quality Gates

- Targeted core checks: run the focused `core` tests that cover `ComputableValue`, `Observer`, `Transaction`, and `ObservableValue`.
- Targeted integration checks: run memoize/nested-computable integration tests affected by generated `@Memoize` accessors.
- Required full gate before merge: `bundle exec buildr ci J2CL=no`.
- Current setup note: `bundle exec buildr --tasks` could not run in this workspace because Bundler reported missing gem `braid-1.1.6`. Run `bundle install` before implementation validation if the environment still has that gap.

## Acceptance Criteria

- An `A -> B -> C` chain remains direct `A -> B` and `B -> C` after no-change lazy validation; no leaked `A -> C` dependency remains.
- Suppressed recursive validation cannot poison transaction duplicate-detection state; a later genuine direct read in the same outer tracker is still present in dependencies and observer backlinks.
- Deep changes that alter `B` still cause `A` to become stale/recompute through existing propagation.
- Deep changes suppressed by `B` equality do not recompute `A`.
- Recursive validation never hides real recomputation dependencies for the recomputing computable.
- Dynamic dependency swaps update the recomputing dependency's graph without flattening into the outer tracker.
- Exception paths preserve existing cached-error and new-error behavior.
- Deactivation and hook behavior follow immediate graph edges.
- Memoize and parameterized memoize paths follow the fixed core behavior.

## Known Intentional Divergences

- Spy event counts and fixture order may change where prior behavior depended on leaked direct observer edges.
- Debug output from `WhyRun` may change as a consequence of the runtime graph changing, but `WhyRun` itself should not be filtered.
- Existing tests that directly assert spy dependency or observer graphs may need expectation updates when they were relying on leaked transitive edges.

## Open Questions Register

No open questions remain from the design interview. The plan is still draft until the user reviews the latest artifacts and the approval outcome is recorded.

### Q-01

- status: resolved
- question: Is this a runtime graph correctness issue or only a `WhyRun` presentation issue?
- context: `WhyRun` reads observer/dependency lists from spy info, which proxies the actual runtime graph.
- options: fix core graph; filter `WhyRun` output.
- tradeoffs: core fix addresses scheduling, retention, hooks, and diagnostics; filtering would make spy APIs misleading.
- recommended_default: fix core graph.
- user_decision: accepted recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `40-test-strategy.md`

### Q-02

- status: resolved
- question: Should recursive validation be isolated from the outer tracker?
- context: `Observer.shouldCompute()` currently calls `ComputableValue.get()` while the outer tracker is active.
- options: isolate recursive validation; keep flattened graph.
- tradeoffs: isolation preserves source-level dependency shape; flattening may over-retain and over-schedule.
- recommended_default: isolate recursive validation.
- user_decision: accepted recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `40-test-strategy.md`

### Q-03

- status: resolved
- question: Should actual recomputation still track normally under the recomputing computable?
- context: validation and recomputation are different phases; recomputation must refresh dynamic dependencies.
- options: suppress all nested observations; suppress validation observations only.
- tradeoffs: suppressing recompute tracking would break dynamic dependencies.
- recommended_default: suppress validation observations only.
- user_decision: accepted recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `40-test-strategy.md`

### Q-04

- status: resolved
- question: Should equality comparison remain the only gate for downstream stale propagation?
- context: existing semantics propagate from a computed value only when its own result changes.
- options: preserve equality gate; introduce deep-change shortcut.
- tradeoffs: a deep-change shortcut would create false downstream recomputes and break documented semantics.
- recommended_default: preserve equality gate.
- user_decision: accepted recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `40-test-strategy.md`

### Q-05

- status: resolved
- question: Should exception behavior during validation remain unchanged?
- context: `Observer.shouldCompute()` catches dependency `get()` exceptions and relies on state transitions.
- options: preserve behavior; rethrow or add new propagation rules.
- tradeoffs: changing exception behavior risks breaking existing tests and cached-error semantics.
- recommended_default: preserve behavior.
- user_decision: accepted recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `40-test-strategy.md`

### Q-06

- status: resolved
- question: Should the fix use a normal nested non-tracking transaction?
- context: Arez invariants currently forbid non-computable nested transactions inside computable transactions.
- options: normal nested read-only action; narrow internal observation suppression.
- tradeoffs: normal action conflicts with invariants and write permissions; internal suppression targets the actual leak.
- recommended_default: internal stack-scoped observation suppression.
- user_decision: accepted recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`

### Q-07

- status: resolved
- question: Should suppression apply to all observable reads during recursive validation?
- context: recursive validation should not add computed or plain observables to the outer tracker.
- options: computed-only suppression; total observation suppression while in recursive validation.
- tradeoffs: total suppression is simpler and prevents edge-case leaks; recompute tracking remains normal.
- recommended_default: total suppression during recursive validation.
- user_decision: accepted recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `40-test-strategy.md`

### Q-08

- status: resolved
- question: Should `READ_OUTSIDE_TRANSACTION` computables follow the same recursive suppression rule?
- context: they still report observations when a tracking transaction is active.
- options: include them; exclude them.
- tradeoffs: excluding them leaves a known flattening path.
- recommended_default: include them.
- user_decision: accepted recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `40-test-strategy.md`

### Q-09

- status: resolved
- question: Should tests assert graph shape directly?
- context: the observed failure is an incorrect runtime dependency graph.
- options: assert graph; assert `WhyRun` strings.
- tradeoffs: graph assertions fail closer to the defect; string assertions are formatting-sensitive.
- recommended_default: assert graph directly and ignore `WhyRun` for now.
- user_decision: accepted recommendation, with explicit request to ignore `WhyRun`.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `40-test-strategy.md`

### Q-10

- status: resolved
- question: Should test coverage be comprehensive across varied circumstances?
- context: the issue touches scheduling, equality, dynamic dependencies, exceptions, hooks, and memoize.
- options: minimal regression; broad matrix.
- tradeoffs: broad matrix costs more upfront but reduces risk of reintroducing graph flattening.
- recommended_default: broad matrix.
- user_decision: accepted recommendation and requested fairly comprehensive tests.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `40-test-strategy.md`

### Q-11

- status: resolved
- question: Should compatibility flags preserve flattened dependency behavior?
- context: flattened graph edges are accidental internal behavior.
- options: no compatibility shim; compatibility mode.
- tradeoffs: compatibility mode doubles semantics and preserves a bug.
- recommended_default: no compatibility shim.
- user_decision: accepted recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`

### Q-12

- status: resolved
- question: Should fixture and event-count changes be treated as expected hard-cut fallout?
- context: removing leaked edges can change scheduling, activation, and deactivation event sequences.
- options: update tests to immediate-dependency semantics; preserve old sequences.
- tradeoffs: preserving old sequences would keep accidental behavior alive.
- recommended_default: update fixtures deliberately where evidence shows the change is caused by graph cleanup.
- user_decision: accepted recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `40-test-strategy.md`

### Q-13

- status: resolved
- question: Should the internal API be explicit and stack-safe?
- context: lazy validation can recurse and must restore state after exceptions.
- options: named scoped suppression with depth/save-restore; ad hoc boolean.
- tradeoffs: scoped suppression is easier to audit and safer under recursion.
- recommended_default: explicit stack-scoped internal API.
- user_decision: accepted recommendation.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`

### Q-14

- status: resolved
- question: Should plan approval be a separate gate before implementation?
- context: the user requested a design phase before any implementation.
- options: proceed to implementation after drafting; require explicit review.
- tradeoffs: explicit review protects design intent and matches workflow requirements.
- recommended_default: require explicit user review before plan acceptance or implementation.
- user_decision: implied by request to use structured workflow for next design phase.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

## Review Correction Register

### R1-F1

- source: iterative plan review round 1
- finding: Suppression design must avoid mutating transaction duplicate-detection state.
- assessment: valid
- correction: Requirements now state that suppressed recursive validation reads must not append observations, update last-tracker state, or mutate equivalent "already observed" bookkeeping.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`, `40-test-strategy.md`

### R1-F2

- source: iterative plan review round 1
- finding: Existing graph-facing spy assertion tests need an explicit audit.
- assessment: valid
- correction: Plan now includes an audit task and test-strategy section for existing `getDependencies()`/`getObservers()` assertion surfaces.
- artifacts_updated: `10-implementation-plan.md`, `20-task-board.yaml`, `40-test-strategy.md`

### R1-F3

- source: iterative plan review round 1
- finding: Approval-state recording needed a single source of truth.
- assessment: valid
- correction: Requirements and task board now make `20-task-board.yaml` `plan.status` authoritative, with markdown statuses as mirrors.
- artifacts_updated: `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml`

### R2-F1

- source: iterative plan review round 2
- finding: Graph-audit task listed two nonexistent integration test paths.
- assessment: valid
- correction: Task board and test strategy now use the actual `component_observable` and `observe` package paths and broaden the audit surface to patterns where appropriate.
- artifacts_updated: `20-task-board.yaml`, `40-test-strategy.md`

### R2-F2

- source: iterative plan review round 2
- finding: Review-loop completion state needed explicit closeout rules.
- assessment: valid
- correction: Requirements and task board now require per-round count/evidence updates and define when `PLAN-REVIEW` can complete before approval.
- artifacts_updated: `00-requirements.md`, `20-task-board.yaml`
