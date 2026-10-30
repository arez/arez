# Mapped relationship value keys

## Source

- Accepted contract: this chat's assessment, explicitly confirmed by the user. Add `useEqualsForKeys = false`; opt-in uses standard equality consistently, retains the original target/source, preserves occurrences and lifecycle, and requires stable equality/hash codes.
- Execution authority: the user requested implementation through structured-delivery-workflow and a pushed PR for review. Automatic plan checkpoint; no merge or release authority.
- Consumer evidence: Rose's `ShiftPlanDateModel` recreates immutable `RDate` keys; its existing `CachedRelationship` uses value equality. The targets hold expanded/selected state. Integer-key mappings have the same need but also require separate context support.
- Repository evidence: [cache](../../core/src/main/java/arez/component/internal/MappedRelationshipCache.java), [generator](../../processor/src/main/java/arez/processor/ComponentGenerator.java), [contract](../../docs/specs/mapped-relationships.md), [identity rationale](../../docs/adr/0003-mapped-relationship-identity.md), [testing](../../docs/agents/build-and-test.md).

## Problem and outcome

Recreated equal value keys currently replace mapped targets. Let callers preserve their targets and local state for interchangeable values without changing the existing identity default.

## Scope and constraints

- Add one annotation boolean and propagate it to each generated relationship cache. Apply its policy to reconciliation, duplicates, retention, and reappearance.
- Preserve result-reference guarding, null support, ordering, filtering, lifecycle cleanup, and disposed-target replacement.
- Exclude context parameters, custom hashing/comparison APIs, key extractors, source rebinding, and Rose changes.

## Requirements

- R1: Default matching remains reference identity. With `useEqualsForKeys = true`, equal sources share a live target across calculations, including retained reappearance.
- R2: Equal occurrences repeat the same target reference; the first source needing construction supplies the factory input. Reuse retains the original source and does not rerun the factory. Equality/hash codes must stay stable while cached or retained, and equal keys must be interchangeable for construction.
- R3: Both policies preserve existing target/result identity, disposal, observation, cleanup, null, presentation and ordering guarantees.
- R4: Publish the contract in API docs, guide, specification, existing identity rationale and changelog. Deliver reviewed code through a pushed PR.

## Acceptance criteria

- AC1 (R1,R2): Generated JVM and browser scenarios demonstrate recreated equal keys, duplicate occurrences, retained reappearance, and preserved local state; existing identity behavior stays covered.
- AC2 (R3): Focused real-component tests cover null, unequal hash collisions, disposed replacement, result-reference guarding and deactivation cleanup in equality mode. Browser coverage runs in development and optimized modes.
- AC3 (R1,R4): Annotation default and exact generated outputs verify propagation. Documentation explains unchanged original source and stable equality requirements; links validate.
- AC4 (R1-R4): `tools/check.sh` passes, independent implementation review passes, temporary plan is removed, and final commits are pushed to a review PR.

## Significant decisions

| ID | Decision | Rationale | Impact | User verification |
| --- | --- | --- | --- | --- |
| D1 | Opt in using a boolean; identity stays default | Existing source-bound targets require identity; Rose values are interchangeable | New option adds value matching without changing existing callers | Confirm identity and value-key examples |
| D2 | Reuse original target/source with stable equality | No rebinding contract exists; preserves local state | Equality mode is unsuitable when equal keys are not interchangeable | Review API caveat and tests |

## Technical and testing decisions

- Select `HashMap` or `IdentityHashMap` for the cache at construction and each rebuild; keep the existing encounter-order list and result identity comparator. No compatibility overload is needed for the internal constructor.
- Primary implements sequentially. Clean read-only subagents review planning and implementation under the skill's bounded review policy.
- Extend existing integration/browser fixtures and a generated-output fixture; avoid new infrastructure or mock lifecycle tests. Boxed integer coverage must verify distinct equal wrappers so boxing caches cannot hide a regression.
- Durable classification: R1-R3 update the existing mapped-relationship specification (accepted, observable, implementation-independent obligations); D1-D2 qualify the existing identity ADR. API usage belongs in Javadoc/guide; implementation details stay in code; validation evidence stays in tasks/PR. Context support stays outside this invocation.

## Open questions

None.
