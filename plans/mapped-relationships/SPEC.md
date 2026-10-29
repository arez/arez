# Mapped Relationships Spec

## Source

- Accepted scope: this chat's completed grill-me interview, explicitly confirmed by the user before invoking structured-delivery-workflow.
- Execution authority: the user selected design followed by implementation, confirmed the contract, then requested this workflow and a review PR. No human plan-review checkpoint or stopping budget was requested.
- Background: [Arez issue 106](https://github.com/arez/arez/issues/106); Rose commit `0c739c09fe2276852e06caa50be8f707c877bb78`, CachedRelationship, KeepableCachedRelationship and callers.
- Repository evidence: [memoization](../../core/src/main/java/arez/annotations/Memoize.java), [processor](../../processor/src/main/java/arez/processor/ArezProcessor.java), [generator](../../processor/src/main/java/arez/processor/ComponentGenerator.java), [runtime](../../core/src/main/java/arez/ComputableValue.java), [development](../../docs/development.md), [publication boundary](../../docs/adr/0002-publication-artifact-boundary.md), [nullability](../../docs/specs/processor-nullability.md).

## Problem

Applications repeatedly reconcile domain collections into observable view-model collections. Ordinary memoization caches a whole result but does not preserve individual target identities when sources are added, removed or reordered. Copied downstream helpers have inconsistent duplicate handling, hook collisions and unsafe equality behavior.

## Required outcome

An Arez component can declare a generated mapped relationship with predictable source identity, reactive filtering and sorting, optional retention after source removal, and normal memoized scheduling and lifecycle. Public documentation and compiled examples explain correct target ownership.

## Scope

- In scope: a standalone @MappedRelationship annotation; five optional companion annotations; internal reconciliation support; processor validation and generation; runtime, integration, golden and browser tests; user documentation, durable contract, examples and changelog; reviewed PR.
- Initial declarations are abstract zero-argument methods returning non-null List<Target>. Source callbacks supply non-null Stream<Source>, with nullable elements supported. Targets are non-null observable Arez components.
- Out of scope: changing Rose, general public cache helpers, parameterized/context-parameter mapped relationships, arbitrary stream transformations, business-key matching, explicit target ownership/disposal policies, publication or merging the PR.

## Requirements

- R1: A mapped relationship reconciles sources by reference identity. It reuses live targets across successful evaluations, recreates disposed targets for present sources, preserves source occurrence order and multiplicity, and supports a null source when its factory accepts null.
- R2: @MappedRelationship is incompatible with @Memoize and has all nine Memoize parameters with the same declared defaults: name, keepAlive, priority, observeLowerPriorityDependencies, depType, reportResult, readOutsideTransaction, writeOutsideTransaction, equalityComparator. Existing effective defaults, references, lifecycle hooks and constraints apply where compatible with a zero-argument mapped property.
- R3: For effective property name attributes and target AttributeModel, infer attributesStream(), createAttributeModel(Source), filterAttributeModel(Target), attributesComparator(), retainAttributeModel(Target). Source/factory are required; other callbacks are optional. Use the simple declared target type name, without singularization.
- R4: @MappedRelationshipSource, Factory, Filter, Comparator and Retention each explicitly associate one callback with exactly one relationship using a single String name. An explicitly associated callback does not implicitly associate with other relationships. Unannotated conventional callbacks may serve compatible relationships. A callback must be concrete and accessible to the generated subclass. Resolve inherited generic signatures and JSpecify contracts. Ignore nonmatching overloads, reject multiple valid candidates, and diagnose a missing required callback with expected and same-named rejected signatures. Explicitly annotated invalid callbacks are errors.
- R5: Source filtering belongs in the source stream and can prevent creation. Presentation filtering runs on mapped targets. Filtered-out targets stay cached and observed while their source remains present, allowing state preservation and reactive visibility. Sorting uses an optional Comparator<Target> callback, evaluated reactively per computation; no short-circuiting target transformations are exposed.
- R6: An optional retention predicate retains live absent-source targets in the result and keeps observing them. Test disposal before reading retention state. With no comparator, present sources precede absent retained targets in previous encounter order. Retained targets pass through the same presentation filter and sorting. Target dependency configuration remains responsible for surviving source disposal, e.g. nullable SET_NULL dependency plus snapshots.
- R7: Each generated relationship owns one cache shared through its getter, not a public independently shareable helper. Cache cleanup occurs on computation deactivation and owner disposal, regardless of collection wrapping. keepAlive retains the computation until owner disposal. Clearing the cache does not explicitly dispose targets; existing target observation/disposeOnDeactivate contracts apply.
- R8: Result equality requires the same target references in the same order and occurrence count, AND the configured comparator's equality. Custom comparison can force extra notifications but cannot suppress identity, order or count changes. Do not alter general ComputableValue equality semantics.
- R9: Callback failures propagate through normal Arez computed error handling. No rollback or preservation of all prior target identities is promised. The cache must remain coherent and releasable after supplier, factory, predicate or comparator failure; the factory owns cleanup before a failed construction returns.
- R10: Ship meaningful extensive tests, public Javadoc, a user guide with compiled examples, discoverability links and an Unreleased changelog entry. Preserve publication and nullability contracts and pass the repository's full gate before opening the PR.

The durable requirements are promoted to the [mapped relationship specification](../../docs/specs/mapped-relationships.md).

## Acceptance criteria

- AC1 (R1,R7,R9): Real Arez lifecycle tests demonstrate stable identity, equal-but-distinct sources, duplicates, null, additions/removals/reorder, disposed-target replacement, independent relationships, final observer release/reactivation, owner disposal and failed evaluation recovery/cleanup.
- AC2 (R2,R3,R4): Processor tests and generated code demonstrate conventional and explicit callback binding, ignored unrelated overloads, valid-candidate ambiguity, actionable signature diagnostics, annotation incompatibility, inherited/compiled generic nullness, all copied settings, refs and hooks, and rejection of unsupported declarations.
- AC3 (R5,R6,R7): Real generated components demonstrate reactive filters and changing comparators, hidden target state preservation, retained absent targets, release of retention, filtered retained targets, source disposal survival and cache cleanup with collection wrapping disabled.
- AC4 (R8): An equal-but-distinct or disposed replacement target reaches consumers despite a permissive comparator; unchanged reference sequences suppress notifications unless the configured comparator requests them. Reorder and duplicate count changes propagate.
- AC5 (R10): Compiled examples and JVM/J2CL browser scenarios in development and optimized modes exercise the annotation, the guide's includes resolve, links validate, and tools/check.sh passes. A PR links issue 106 and explains non-obvious contracts.

## Significant decisions

| ID | Decision | Rationale | Impact | User verification |
| --- | --- | --- | --- | --- |
| D1 | Generate a standalone mapped property with all memoization options | User selected convention-driven annotation and rejected @Memoize composition | Public annotation duplicates option declarations, while internal generation reuses existing memoization | Review annotation usability and diagnostics |
| D2 | Type-based factory/filter/retention conventions; property-based stream/comparator | User's naming correction | Same-type relationships can share conventional callbacks, or use explicit per-property callbacks | Review names and explicit-association examples |
| D3 | Identity keys and guarded result equality ([ADR](../../docs/adr/0003-mapped-relationship-identity.md)) | A target captures the source instance; ordinary List.equals can retain stale/disposed targets | Custom comparator authority is narrower than Memoize | Review replacement and notification tests |
| D4 | Observe hidden targets; retain absent ones by predicate | Preserves Rose's view-model state and visible-row use cases | Source filtering avoids creation; target filtering cannot always do so | Review lifecycle example and resource behavior |
| D5 | Explicit companions bind one property, without arrays/repeatability | User's final interview choice C | Shared irregular behavior uses delegating callbacks | Review public companion API |

## Technical decisions

- Extend the existing memoized descriptor/generator with a generated calculation body; retain existing non-mapped behavior and goldens.
- Put reconciliation/equality support in the existing core internal component package, callable by generated components. Use no new publication artifact and no new general runtime option.
- Use identity lookup plus explicit encounter order. Consume finite source streams sequentially; no reliance on identity-map iteration order.
- Use effective method inventories and Types.asMemberOf rather than name-only maps for callback resolution; diagnostics must not mistake unrelated overloads for candidates.
- Preserve ordinary Java override precedence before callback discovery. Signature compatibility includes source-to-factory contravariance, target return assignability and safe nullness; competing compatible candidates are an error rather than an arbitrary overload choice.
- Conventional optional nonmatching methods remain unbound; required-method diagnostics list same-name alternatives. Explicit companion association is validated strictly.
- Reuse normal Arez collection wrapping and default resolution; mapped cleanup is mandatory in either wrapping configuration. Resolve target names from declared element types; unsupported unresolved/wildcard target contracts fail clearly rather than guessing a naming stem.
- Promote durable behavior to a concise specification and API documentation; delete this entire temporary plan tree after implementation review passes.

## Testing decisions

- Use real observables, computables, observers and generated components; avoid lifecycle mocks. Retain narrow internal algorithm tests only where they protect identity/order/failure behavior.
- Add a mapped-relationship processor family with diagnostics and exact generated output in both formatting modes. Keep source/compiled generic/nullness parity coverage with the owning processor tests.
- Exercise copied settings and cleanup under native components/names/collection wrapping variants using existing test support. Add a small first-party browser scenario to the existing browser aggregate for J2CL identity/null/retention behavior in development and production.
- Run focused slices first, then Java/build formatting, documentation/link checks, packaged downstream checks and tools/check.sh. Fix new coverage gaps with behavioral tests, not exclusions or weakened floors.

## Open questions

None.
