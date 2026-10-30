# Mapped Relationships Specification

## Purpose

Define the generated relationship that maps source objects to observable Arez
components while preserving each live target's identity and local state.

## Requirements

1. `@MappedRelationship` declares an abstract, zero-argument method returning a
   non-null `List<Target>` with a declared, non-null target type. Observability
   follows the [component target validation contract](component-target-validation.md).
2. The annotation is incompatible with `@Memoize` and exposes all its parameters
   with the same declared defaults. Compatible computation references, hooks,
   component defaults and scheduling rules have the same behavior.
3. For effective property name `attributes` and target type `AttributeModel`,
   discover required `attributesStream(): Stream<Source>` and
   `createAttributeModel(Source): AttributeModel` callbacks.
4. Discover optional `filterAttributeModel(AttributeModel): boolean`,
   `attributesComparator(): Comparator<? super AttributeModel>` and
   `retainAttributeModel(AttributeModel): boolean` callbacks. The target naming
   stem is its simple declared type name.
5. Each companion annotation associates its callback with exactly one property
   using a required single `String name`. Explicitly associated callbacks do not
   implicitly bind to other properties. Unannotated conventional callbacks may
   serve multiple compatible properties.
6. Callbacks must be concrete instance methods accessible to the generated
   subclass, without method type parameters or declared exceptions. Validate
   parameter types, return types and JSpecify contracts in the effective inherited
   context, for both source and compiled declarations.
7. Ignore incompatible conventional overloads. Reject multiple compatible
   candidates and invalid explicit callbacks. Missing required callback diagnostics
   describe the expected signature and any same-named rejected methods.
8. Consume and close the finite non-null source stream sequentially in encounter
   order. Nullable source elements are permitted when the factory accepts null;
   factory results must be non-null.
9. Match sources by reference identity, including null. Reuse live targets for
   sources that remain present; recreate disposed targets for present sources.
10. Preserve repeated source occurrences as repeated references to the same target
    within one calculation.
11. Source-stream filtering may prevent target creation. Presentation filtering
    runs after mapping; hidden targets remain cached and observed while their
    source is present.
12. Evaluate the optional comparator provider during each calculation and sort the
    presented targets with its non-null result. Observe reactive reads made by
    callbacks, stream operations and comparisons.
13. Retain a live absent-source target when the optional retention predicate
    returns true. Check target disposal before invoking the predicate.
14. Without sorting, return present targets in source encounter order, then absent
    retained targets once per source in their previous encounter order. Retained
    targets pass through the same presentation filter and sorting.
15. Reuse a retained target if its source reappears. Release its cached reference
    when its source remains absent and retention becomes false.
16. Retention does not override target component dependencies. Surviving source
    disposal requires a target contract that permits it, such as a nullable
    `SET_NULL` dependency with snapshots of required source data.
17. Each property owns an independent cache, shared by callers through its getter.
    Deactivation and owner disposal release cached references, regardless of
    collection wrapping. `keepAlive` retains normal memoized lifecycle behavior.
18. Cache cleanup does not explicitly dispose targets. Target observation and the
    target's own lifecycle policy govern target disposal.
19. Results are equal only when their target references, order and occurrence
    count match and the configured equality comparator returns true. Custom
    comparison may force additional notifications but cannot suppress an instance,
    order or count change.
20. Callback failures use normal computed error handling. Subsequent evaluation
    and cleanup must remain possible; rollback and preservation of prior target
    identities are not guaranteed. A factory is responsible for cleanup of
    construction that fails before returning a target.

## Architectural basis

[Reference identity and guarded equality](../adr/0003-mapped-relationship-identity.md).
The [processor nullability contract](processor-nullability.md) also applies.
