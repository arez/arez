# Implementation Plan

## Phase Sequence

1. Create structured-delivery artifacts under `plans/multi-on-deps-change/`.
2. Refactor observe descriptor storage and processor linking to keep ordered multi-hook `@OnDepsChange` state.
3. Update observe code generation to preserve single-hook output and synthesize a dispatcher for multi-hook observes.
4. Add/refresh processor fixtures and tests for legal multi-hook, mixed-signature, inherited, convention-derived, and error scenarios.
5. Update `CHANGELOG.md`.
6. Run processor verification gates and capture evidence in the task board.

## Delivery Approach

- Keep the runtime contract unchanged by solving the feature entirely in the annotation processor and generated code.
- Localize list-shape behavior inside `ObserveDescriptor` helper methods so validation and generation stay readable.
- Preserve current generated output for the single-hook case to minimize regression risk and expected-fixture churn.
- Use deterministic encounter order from method scanning as the sole ordering rule and reflect that in fixtures.

## High-Risk Areas

- Risk 1: multi-hook linkage could break existing unresolved-observe diagnostics.
  - Mitigation: keep a stable first-hook anchor for missing-observe errors and add explicit bad-input coverage.
- Risk 2: mixed parameterless/`Observer` hook sets could generate the wrong callback shape.
  - Mitigation: add dedicated fixtures for mixed signature sets on both internal/external observe cases.
- Risk 3: inherited and convention-derived hooks could regress due to method discovery assumptions.
  - Mitigation: add fixtures that combine base-type hooks, subtype hooks, and convention-derived hooks.
- Risk 4: single-hook fixture output could change unintentionally during generator refactoring.
  - Mitigation: constrain new code paths to the multi-hook case and rerun the existing processor fixture suite.

## Required Full Gates

- `bundle exec buildr processor:test`

## Decision Log

| question | concrete plan change |
| --- | --- |
| Q-01 | descriptor and generator will support ordered lists of matched hooks rather than singular storage |
| Q-02 | processor will preserve encounter order and generator will emit dispatcher calls in that order |
| Q-03 | multi-hook generation will use one private framework dispatcher that can call mixed signatures |
| Q-04 | dispatcher bodies will call hooks sequentially without swallowing exceptions |
| Q-05 | single-hook generation paths remain intact and only multi-hook observes use the new dispatcher path |
| Q-06 | annotated and convention-derived hooks will be merged into the same per-observe ordered list |
| Q-07 | internal-executor validation will treat any observer source in the hook set as sufficient for schedulability |
| Q-08 | auto-linked `onXDepsChange(...)` methods remain eligible and additive in multi-hook scenarios |
| Q-09 | unresolved linkage errors will be emitted once per logical observe using the first matched hook as the anchor |
| Q-10 | inherited hooks will be additive whenever both methods appear in the discovered method set |

## Completion Criteria

- `ObserveDescriptor` supports ordered multi-hook storage with clear helper accessors.
- Processor linkage accepts multiple matched hooks and preserves deterministic encounter order.
- Generated code is unchanged for single-hook observes and emits one dispatcher for multi-hook observes.
- Processor fixtures cover legal multi-hook combinations and remaining invalid linkage cases.
- `CHANGELOG.md` documents support for multiple `@OnDepsChange` hooks per observe.
- `bundle exec buildr processor:test` passes.
