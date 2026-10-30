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
9. Match sources by reference identity by default, including null.
   `useEqualsForKeys` defaults to false; enabling it uses standard `equals` and
   `hashCode` semantics consistently for reconciliation, duplicates and retention.
   Reuse live targets for matching present sources; recreate disposed targets.
10. Preserve repeated matching source occurrences as repeated references to the
    same target within one calculation.
11. Source-stream filtering may prevent target creation. Presentation filtering
    runs after mapping; hidden targets remain cached and observed while their
    source is present.
12. Evaluate the optional comparator provider during each calculation and sort the
    presented targets with its non-null result. Observe reactive reads made by
    callbacks, stream operations and comparisons.
13. Retain a live absent-source target when the optional retention predicate
    returns true. Check target disposal before invoking the predicate.
14. Without sorting, return present targets in source encounter order, then absent
    retained targets once per matching key in their previous encounter order. Retained
    targets pass through the same presentation filter and sorting.
15. Reuse a retained target if a matching source reappears under the selected key
    policy. Release its cached reference when its source remains absent and
    retention becomes false.
16. Retention does not override target component dependencies. Surviving source
    disposal requires a target contract that permits it, such as a nullable
    `SET_NULL` dependency with snapshots of required source data.
17. Each computation owns independent relationship state, shared by readers of
    the same property and context key. Different context keys do not reuse each
    other's targets or retained entries. Deactivation releases only that
    computation's cached references; owner disposal releases all relationship
    state, regardless of collection wrapping. Unobserved reads do not promise
    target identity across transactions.
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
21. Equality matching requires equal keys to be interchangeable for target
    construction, with stable equality and hash codes while cached or retained.
22. Reusing a target preserves its original source object without rebinding or
    invoking the factory again.
23. When construction is required, the first matching source encountered supplies
    the factory argument.
24. Matching `@MemoizeContextParameter` declarations capture implicit getter
    inputs, including inherited declarations and effective generic types from
    source and compiled dependencies. The getter still accepts no explicit
    arguments.
25. The complete captured context tuple identifies the computation using ordinary
    memoization's `equals`/`hashCode` semantics, including nullable values.
26. Restore captured values around source creation and traversal, factories,
    presentation filtering, comparator creation and comparisons, retention and
    stream closure. Pop contexts in reverse push order, including when evaluation
    fails, restoring the caller's ambient context.
27. `@ComputableValueRef` captures the same implicit context values as its getter
    and selects that context's computation.
28. Disposal of a captured context that implements `DisposeNotifier` releases the
    computations using it, without releasing unrelated contexts.
29. Contextual relationships reject `keepAlive = true` and user activation or
    deactivation hooks, matching contextual `@Memoize` restrictions. Context-free
    `keepAlive` retains normal memoized lifecycle behavior.
30. Context annotations on class overrides take precedence over declarations on
    inherited interface defaults, including patterns that exclude properties.

## Architectural basis

[Reference identity and guarded equality](../adr/0003-mapped-relationship-identity.md).
[Independent contextual ownership](../adr/0004-contextual-relationship-ownership.md).
The [processor nullability contract](processor-nullability.md) also applies.
