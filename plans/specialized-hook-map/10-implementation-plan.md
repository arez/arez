# Implementation Plan

## Phase Sequence

1. Create structured-delivery artifacts under `plans/specialized-hook-map/`.
2. Add package-private `HookMap` and switch `Observer`/`Transaction` hook storage and iteration to the specialized type.
3. Update hook-focused core tests and add dedicated `HookMapTest` coverage.
4. Update `CHANGELOG.md`.
5. Run targeted core, full repo, and downstream size verification gates.
6. If downstream size fixtures change and are acceptable, update them and rerun downstream size checks.

## Delivery Approach

- Keep the specialization narrowly scoped to hook storage so the refactor is behavior-preserving and easy to audit.
- Prefer a compact linear implementation over a generic hash structure because hook counts are expected to stay small and JS output size matters more than large-map asymptotics here.
- Remove hook-path use of `Map`, `Map.Entry`, and stream pipelines so the transpiled output only includes the minimal operations actually used.

## High-Risk Areas

- Risk 1: insertion order could change when replacing an existing key.
  - Mitigation: implement replacement-in-place semantics and add dedicated order tests.
- Risk 2: hook deactivation behavior could change if iteration/clear ordering shifts.
  - Mitigation: keep existing behavioral tests and add targeted assertions around deactivation order and removed-hook callbacks.
- Risk 3: downstream size checks may fail even if core tests pass.
  - Mitigation: run `buildr test_downstream_build_stats` after the full test gate and only update stored statistics if the new size is acceptable.

## Required Full Gates

- `bundle exec buildr test`
- `buildr test_downstream_build_stats`

## Decision Log

| question | concrete plan change |
| --- | --- |
| Q-01 | include downstream size verification in the implementation gate and plan for fixture/statistics updates only if the measured size change is accepted |

## Completion Criteria

- `LinkedHashMap` is removed from `Observer` and `Transaction`.
- `HookMap` provides the required semantics with dedicated unit coverage.
- Hook registration/deactivation behavior remains unchanged in existing core tests.
- `CHANGELOG.md` records the runtime size optimization.
- Full verification and downstream size checks pass, or any blocking failures are documented with cause.
