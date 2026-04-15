# Implementation Plan

## Phase Sequence

1. Create and maintain structured-delivery artifacts under `plans/misplaced-annotation-validation/`.
2. Expand processor discovery and add a reusable covered-annotation list in the processor.
3. Implement a round-level misplaced-usage scan before `@ArezComponent` type processing.
4. Add processor fixtures and assertions for every covered misplaced annotation, including method/field variants and type-level `@SuppressArezWarnings` edge cases.
5. Update changelog and affected annotation Javadocs.
6. Run verification gates and record evidence in the task board.

## Delivery Approach

- Keep the new behavior entirely in processor diagnostics; do not change component parsing or generation.
- Centralize the covered-annotation list so discovery and validation stay in sync.
- Use fixture-based processor tests to prove every covered annotation reports the misplaced-usage error when compiled outside a valid Arez container.

## High-Risk Areas

- Risk 1: the processor may not run for source sets that only contain misplaced annotations.
  - Mitigation: expand supported annotation types to include the full covered set and add a test with no `@ArezComponent` in the compilation unit.
- Risk 2: the scan may incorrectly reject valid use inside nested members of `@ArezComponent` or `@ArezComponentLike` types.
  - Mitigation: resolve the nearest enclosing type via parent traversal and add positive fixtures for `@ArezComponentLike`.
- Risk 3: inherited-member exemptions may accidentally allow type-level `@SuppressArezWarnings` on non-component supertypes.
  - Mitigation: distinguish between annotated types and annotated members when validating the enclosing container.
- Risk 3: the covered-annotation list may drift from actual processor handling.
  - Mitigation: derive the list from the same constants used by the processor and keep exclusions explicit.

## Required Full Gates

- `bundle exec buildr test`

## Decision Log

| question | concrete plan change |
| --- | --- |
| Q-01 | allow covered annotations inside `@ArezComponentLike` types without making those types processable components |
| Q-02 | do not add `@ReferenceTarget` or expand `@ArezComponentLike` semantics in this change |
| Q-03 | scan the broad processor-consumed annotation set, include `@SuppressArezWarnings`, and exclude `@ArezComponent`, `@ArezComponentLike`, and `@DefaultEqualityComparator` |

## Completion Criteria

- Misplaced covered annotations fail compilation with a consistent error message.
- Every covered annotation has processor-test coverage, with separate method/field fixtures where both placements are supported.
- Existing `@ArezComponent` behavior remains unchanged.
- Type-level `@SuppressArezWarnings` is accepted only on directly annotated `@ArezComponent`/`@ArezComponentLike` types.
- `@ArezComponentLike` fixtures do not trigger the new misplaced-usage error.
- Changelog and affected Javadocs describe the new validation behavior.
