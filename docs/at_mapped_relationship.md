---
title: @MappedRelationship
---

Use {@link: arez.annotations.MappedRelationship @MappedRelationship} to turn a
stream of source objects into a reactive list of view models. Adding or reordering
sources preserves existing target instances and their local state.

The getter must be abstract, accept no parameters and return a non-null
`List<Target>`. Targets must be non-null observable Arez components. Source and
factory callbacks are required; presentation and retention callbacks are optional.
For a property named `attributes` with target type `AttributeModel`, the processor
uses these conventions:

| Role | Method | Contract |
| --- | --- | --- |
| Source | `attributesStream()` | Non-null `Stream<Source>` |
| Factory | `createAttributeModel(Source)` | Non-null `AttributeModel` |
| Presentation | `filterAttributeModel(AttributeModel)` | `boolean`; false hides the target |
| Sorting | `attributesComparator()` | Non-null `Comparator<? super AttributeModel>` |
| Retention | `retainAttributeModel(AttributeModel)` | `boolean`; true retains a live absent-source target |

The property name comes from the getter or the annotation's `name` parameter.
The target stem is the simple type name, without singularization. Callbacks must
be concrete instance methods accessible to the generated subclass, without method
type parameters or declared exceptions. Compatible parameter and return types,
including inherited generic and JSpecify contracts, determine which methods bind.
Unrelated overloads are ignored; multiple compatible candidates are an error.

This directory preserves row instances, sorts by display name, hides rows and
retains pinned rows when people leave the directory:

{@file_content: file=arez/doc/examples/at_mapped_relationship/DirectoryModel.java start_line=@ArezComponent "end_line=^}"}

Read `getRows()` from an observer or another computed property. Change observable
state within an [action](actions.md). Remove the three optional callbacks for a
basic mapping that follows source order and releases absent targets.

When the declared target is a shared `@ArezComponentLike` abstract class or
interface that hides its generated observability, set
`@MappedRelationship(validateTypeAtRuntime = true)`. Framework annotations marked
with `@ActAsArezComponent` also qualify. Keep callback signatures expressed in
the declared target type; the actual factory result must implement
`ComponentObservable`. Every target is observed before presentation filtering,
and retained targets remain observed. A filter cannot hide an invalid factory
result from validation.

Without opt-in, the processor must establish observability at compile time.
Arbitrary `Object` and unrelated DTO declarations are rejected with either flag
value. Opt-in on a type whose observability is already established produces the
suppressible warning `Arez:UnnecessaryRuntimeTypeValidation`; remove the flag or
suppress that warning when keeping it is intentional. Runtime failures use the
existing observation checks. Disabling API invariants preserves the cast and
observation attempt, while diagnostics depend on the runtime and build mode.

## Creation and presentation

Sources are matched by reference identity by default. Two equal but distinct
source objects get distinct targets. Repeating the same source produces repeated references to
one target. A null source is also a valid key when the stream element and factory
parameter explicitly allow null; it still maps to a non-null target.

For interchangeable value keys such as immutable dates or boxed integers, set
`@MappedRelationship(useEqualsForKeys = true)`. Matching then uses standard
`equals()` and `hashCode()` consistently for reconciliation, duplicate occurrences
and retention. Recreated equal keys reuse existing live targets and their local
state. Repeated equal keys produce repeated references to one target.

Equal keys must be interchangeable for target construction, and their equality
and hash codes must remain stable while cached or retained. Reuse keeps the
original target and its original source object; it does not rerun the factory or
rebind the target to a newer equal source. When a target needs to be created, the
first matching source encountered supplies the factory argument. Keep identity
matching when equal source objects carry different instance-specific state.

Filter sources in the stream when excluded sources should have no target. A
presentation filter runs after creation: hidden targets remain cached and observed,
preserving local state and allowing changes in visibility to bring them back.
This also keeps their reactive resources active. The finite stream is consumed
sequentially and closed after traversal.

The comparator provider runs on every calculation. Reactive reads in stream
operations, predicates, the provider and its comparisons are tracked. Without a
comparator, current source occurrences come first, followed by absent retained
targets in previous encounter order, once per matching key. Retained targets use the
same presentation filter and sorting as current targets.

## Retention and source disposal

Retention preserves a live target after its source leaves the stream. A retained
target is reused if a matching source returns, using reference identity by default
or value equality when `useEqualsForKeys` is enabled. It is released when retention
becomes false. Disposed targets are never passed to the retention predicate; a disposed
target for a present source is recreated.

Target dependencies still control what happens when a source is disposed. A
default cascading dependency disposes its target even when retention is true.
To show a pinned row after deletion, use a nullable `SET_NULL` dependency and
capture the data needed after the source disappears:

{@file_content: file=arez/doc/examples/at_mapped_relationship/PersonRow.java start_line=@ArezComponent "end_line=^}"}

Remove the person from the source collection and dispose it in the same action.
The row's dependency becomes null, its snapshot supplies the display name, and
`isPinned()` determines whether it remains in the relationship.

Each getter owns an independent cache shared by its readers. When the computation
deactivates or its owner is disposed, cached references are released, including
hidden and retained entries. Cache cleanup does not explicitly dispose targets;
`disposeOnDeactivate = true` on the target commonly supplies that lifecycle.
Disabling collection wrapping does not disable cleanup. `keepAlive = true` keeps
the computation active until owner disposal.

## Explicit callback names

Use {@link: arez.annotations.MappedRelationshipSource @MappedRelationshipSource},
{@link: arez.annotations.MappedRelationshipFactory @MappedRelationshipFactory},
{@link: arez.annotations.MappedRelationshipFilter @MappedRelationshipFilter},
{@link: arez.annotations.MappedRelationshipComparator @MappedRelationshipComparator}
or {@link: arez.annotations.MappedRelationshipRetention @MappedRelationshipRetention}
when a callback name differs from the convention. Each annotation requires one
`name` identifying the effective property. An explicit callback binds only that
property, even if its method name follows another property's convention.

Unannotated conventional methods may serve several compatible properties. Share
an irregularly named implementation through delegating callbacks:

{@file_content: file=arez/doc/examples/at_mapped_relationship/DualDirectory.java start_line=@ArezComponent "end_line=^}"}

The two getters have separate target instances and lifecycle even though their
factories share an implementation. Use one canonical getter and delegate aliases
to it when readers should share the same relationship.

## Memoization and failures

`@MappedRelationship` exposes the same settings and defaults as
[@Memoize](at_memoize.md), including scheduling, transaction policies, external
invalidation, computation references and lifecycle hooks. Use it by itself;
combining it with `@Memoize` is a compile-time error. See the
{@link: arez.annotations.MappedRelationship API} for each setting.

Result equality always checks target references, order and occurrence count.
An `equalityComparator` is consulted only when those match; returning false can
request an extra notification. Returning true cannot suppress a replacement,
reorder or count change, even when targets are equal according to `equals()`.

Callback failures follow normal computed error handling. The next calculation can
retry, but rollback and preservation of prior targets are not guaranteed. A factory
must clean up construction that fails before returning a target. Deactivation still
releases targets from partially completed calculations.
