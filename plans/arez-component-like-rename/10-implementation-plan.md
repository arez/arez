# ArezComponentLike Rename Implementation Plan

## Phase Sequence

1. Create and maintain structured workflow artifacts under `plans/arez-component-like-rename/`.
2. Rename the public annotation type/source file and update core annotation Javadocs to `@ArezComponentLike`.
3. Rename processor and persist-processor constants/helpers/diagnostics while preserving component-like validation-only behavior.
4. Rename impacted tests, fixtures, expected outputs, and doc examples so terminology is consistent and generated-output names stay aligned.
5. Update changelog, api-test metadata, and prior active workflow artifacts that would otherwise remain inaccurate.
6. Run targeted and full verification, then record evidence and any unrelated failures.

## Delivery Approach

- Treat the change as a hard API rename with no compatibility shim.
- Keep behavior intact by only renaming the marker annotation and terminology, not altering the existing validation decision points.
- Rename test fixture files/classes and expected outputs together so fixture resolution and generated names remain synchronized.
- Update prior workflow artifacts that still describe the current behavior with the old annotation name so repository documentation stays accurate after the rename.

## High-Risk Areas

- Risk 1: a hard rename could leave mixed public terminology in diagnostics, Javadocs, or fixture names.
  - Mitigation: perform a broad repository search, rename embedded file/class names, and finish with a zero-results audit for the former annotation name outside intentionally historical changelog text.
- Risk 2: helper/constant renames could accidentally alter the component-like validation behavior.
  - Mitigation: keep logic structure intact while renaming helpers, then verify the existing component-like positive/negative processor coverage.
- Risk 3: expected-output names could drift from renamed fixture class names.
  - Mitigation: rename fixture files, class declarations, and expected generated output files in the same pass, then run `arez:processor:test`.
- Risk 4: repo workflow artifacts from earlier changes could remain misleading after the rename.
  - Mitigation: update active plan docs that reference the annotation semantics and rename terminology where those docs describe the current codebase.

## Required Full Gates

- `bundle exec buildr test`

## Decision Log

| question | concrete plan change |
| --- | --- |
| none | proceed directly with the user-specified hard rename, no alias, no semantic expansion |

## Completion Criteria

- No current source, tests, docs, or active workflow artifacts refer to the former annotation name.
- `@ArezComponentLike` remains validation-only and is not treated as an `@ArezComponent` processing entrypoint.
- Misplaced-annotation validation, `@AutoObserve(validateTypeAtRuntime = true)`, `@ComponentDependency` compatibility, and unmanaged component-reference warnings still accept component-like targets.
- Type-level `@SuppressArezWarnings` remains restricted to directly annotated `@ArezComponent`/`@ArezComponentLike` types.
- `CHANGELOG.md`, doc examples, Javadocs, and api-test metadata describe the renamed public annotation accurately.
- Verification evidence is recorded, including any unrelated failures from the full gate.
