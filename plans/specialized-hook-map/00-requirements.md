# Specialized Hook Map Requirements

## Mission

- Reduce transpiled JavaScript overhead in hook handling by replacing `LinkedHashMap` usage in `Observer` and `Transaction` with a package-private specialized implementation while preserving runtime behavior.

## Scope Boundaries

- In scope: `core` hook storage/runtime logic, package-private accessors, hook-specific tests, downstream size verification, `CHANGELOG.md`, and structured-delivery artifacts.
- Out of scope: public API changes, processor/extras modules, non-hook collection refactors, and behavior changes to hook registration semantics.

## Locked Decisions

- Introduce a package-private `arez.HookMap` specialized for `String -> Hook`.
- Preserve insertion-order iteration semantics matching the current `LinkedHashMap` usage.
- Use an array-backed linear structure optimized for small hook counts and small transpiled output.
- Keep `Observer._hooks` eagerly allocated and always non-null.
- Keep `Transaction._hooks` nullable and lazily allocated.
- Replace stream- and `Map.Entry`-based hook iteration with indexed loops.
- Update `CHANGELOG.md` because downstream build size is a user-visible outcome.

## Command Surface

- Add `core/src/main/java/arez/HookMap.java` with this internal API:
  - `int size()`
  - `boolean isEmpty()`
  - `void clear()`
  - `boolean containsKey(String key)`
  - `Hook get(String key)`
  - `void put(String key, Hook value)`
  - `String keyAt(int index)`
  - `Hook valueAt(int index)`
- Change package-private hook accessors in `Observer` and `Transaction` from `Map<String, Hook>` to `HookMap`.

## Quality Gates

- Targeted iteration gate:
  - `bundle exec buildr arez:core:test`
- Required full gates:
  - `bundle exec buildr test`
  - `buildr test_downstream_build_stats`
- If downstream size fixtures legitimately change:
  - `buildr update_downstream_build_stats`

## Open Questions Register

| id | status | question | context | options | tradeoffs | recommended_default | user_decision | artifacts_updated |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Q-01 | resolved | How should the code-size/performance goal be validated? | Behavior parity alone would not prove the bundle-size objective. The repo already has downstream size checks. | measure size in-repo; measure locally only; behavior parity only | In-repo measurement adds more work but directly validates the motivating requirement and aligns with existing downstream stats tooling. | measure size in-repo | measure size in-repo | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
