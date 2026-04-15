# Implementation Plan

## Phase Sequence

1. Create and maintain structured-delivery artifacts under `plans/act-as-arez-component-meta-annotation/`.
2. Add a shared processor helper that recognizes framework-managed component-like containers via direct `@ArezComponentLike` or meta-annotations named `ActAsArezComponent`.
3. Route all relevant processor validation paths through the shared helper so the semantics stay consistent.
4. Add fixture-based processor coverage for direct and fallback meta-annotation cases, including package-access visibility.
5. Update `@ActAsArezComponent` Javadocs and `CHANGELOG.md`.
6. Run verification gates and record evidence in the task board.

## Delivery Approach

- Keep the change processor-only for behavior; do not make custom annotations generate Arez components.
- Centralize the new detection logic in `ArezProcessor` so misplaced-annotation validation, runtime-type validation, dependency checks, and unmanaged-reference warnings all use the same definition of component-like.
- Prefer positive fixture coverage that demonstrates the framework integration pattern the user described.

## High-Risk Areas

- Risk 1: only one validation path is updated, leaving other “component-like” checks inconsistent.
  - Mitigation: audit every current use of `@ArezComponentLike` checks and route them through one helper.
- Risk 2: simple-name fallback fails for package-access downstream annotations.
  - Mitigation: inspect annotation mirrors rather than relying on imports or source-level accessibility.
- Risk 3: the fallback unintentionally changes real `@ArezComponent` generation semantics.
  - Mitigation: keep parsing/generation entrypoints keyed to direct `@ArezComponent` only.

## Required Full Gates

- `bundle exec buildr test`

## Decision Log

| question | concrete plan change |
| --- | --- |
| Q-01 | recognize any meta-annotation whose simple name is `ActAsArezComponent`, regardless of package, and add tests for both direct-Arez and downstream-fallback marker annotations |

## Completion Criteria

- Types annotated by a framework annotation marked with `ActAsArezComponent` compile when they contain covered Arez annotations such as `@Action`.
- The same behavior works when the marker annotation only matches by simple name and is package-access in a downstream package.
- Existing `@ArezComponent` code generation behavior remains unchanged.
- Existing direct `@ArezComponentLike` behaviors remain unchanged.
- `CHANGELOG.md` and `@ActAsArezComponent` Javadocs describe the supported integration pattern.
