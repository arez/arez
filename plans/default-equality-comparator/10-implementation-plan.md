# Implementation Plan

## Phase Sequence

1. Create and maintain structured-delivery artifacts under `plans/default-equality-comparator/`.
2. Add the new public annotation, update annotation defaults, and record the API delta.
3. Extend processor parsing, comparator derivation, and comparator validation.
4. Add positive and negative processor fixtures plus generated output expectations.
5. Add integration tests covering derived defaults and explicit overrides.
6. Update docs, examples, and changelog.
7. Run verification gates and close out.

## Delivery Approach

- Keep behavior changes centered in the processor; do not change runtime comparator semantics.
- Resolve the effective comparator once during processing and keep generation logic based on the resolved comparator.
- Add tests for both derivation and fallback so explicit comparator paths remain stable.

## High-Risk Areas

- Risk 1: observable getter/setter pairing may report the wrong failure if comparator derivation happens too early.
  - Mitigation: resolve the effective comparator after type compatibility checks for observables.
- Risk 2: generated code may instantiate invalid comparator classes.
  - Mitigation: validate abstractness, accessibility, and accessible no-arg constructor in the processor.
- Risk 3: public API changes may be missed because annotation default values changed reflectively.
  - Mitigation: update the api diff baseline and run `test_api_diff`.

## Required Full Gates

- `bundle exec buildr test`
- `bundle exec buildr test_api_diff`
- `bundle exec buildr site:build`

## Decision Log

| question | concrete plan change |
| --- | --- |
| Q-01 | derive defaults only from the exact declared type and avoid hierarchy precedence logic |
| Q-02 | keep arrays/JDK/third-party types on explicit `equalityComparator` in v1 |

## Completion Criteria

- All planned tasks completed.
- Processor fixtures cover derive-default, explicit-override, fallback, and invalid comparator cases.
- Integration tests prove type-level defaults for both observables and memoized values.
- API diff, docs, and changelog all reflect the final public behavior.
