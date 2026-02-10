---
title: @Observable
---

The {@link: arez.annotations.Observable @Observable} annotation simplifies writing [observable](observable_values.md) properties.
The simplest use-case involves annotating one of a standard getter/setter pair with the annotation.

For example:

{@file_content: file=arez/doc/examples/at_observable/MyModel.java start_line=@ArezComponent "end_line=^}"}

The component can also be defined using a pair of abstract getters and setters, in which case the annotation
processor will provide a suitable implementation of the getter. This often results in shorter, simpler code.
For example:

{@file_content: file=arez/doc/examples/at_observable2/MyModel.java start_line=@ArezComponent "end_line=^}"}

## Initial values

Abstract observables typically require a constructor parameter to supply the initial value. The
{@link: arez.annotations.ObservableInitial @ObservableInitial} annotation allows an abstract observable to be
initialized from a static field or static method, removing the need for constructor parameters.

The annotated field must be `static final` (or a `static` method) and the associated observable must be abstract.
If the observable getter is annotated with `javax.annotation.Nonnull` then the initializer
must also be annotated with `javax.annotation.Nonnull`.

For example:

{@file_content: file=arez/doc/examples/at_observable_initial/MyModel.java start_line=@ArezComponent "end_line=^}"}

There are additional constraints that are apply when defining observable. i.e. The type of the getter and setter
must match. These constraints are detailed in the {@link: arez.annotations.Observable @Observable} API documentation.

## Customizing names

The `@Observable` annotation methods can be named using non-standard naming conventions and this is sometimes
required to integrate with existing frameworks. In this scenario, the user must add and additional parameter
to the annotation to explicitly name the observable property. This may require annotating both the setter and
the setter with the `@Observable` annotation.

For example:

{@file_content: file=arez/doc/examples/at_observable/Currency.java start_line=@ArezComponent "end_line=^}"}

## Equality Comparison

By default, generated setters compare the new value and current value using
`Objects.equals(...)` before reporting a change. You can customize this behavior via
the `equalityComparator` parameter on the {@link: arez.annotations.Observable @Observable}
annotation.

Common options include:

* {@link: arez.ObjectsEqualsComparator} (default)
* {@link: arez.ObjectsDeepEqualsComparator}
* A custom implementation of {@link: arez.EqualityComparator}

## Custom change propagation

Sometimes you want to integrate Arez into systems that have existing change propagation mechanisms. For
example, Arez was integrated in a java variant of React called [React4j](https://react4j.github.io). React4j
components are configured by passing the component `props`. These components can be re-configured by passing
the component another value for `props`. The react framework is wholly responsible for detecting changes in
`props`, assigning the `props` field and then invoking a callback method on the component to notify the
component that `props` has changed. Making this `props` property observable within Arez involved the following
changes.

* Disabling the `@Observable` setter via the {@link: arez.ObservableValue#expectSetter() @Observable.expectSetter}
  parameter on the getter.
* Accessing the underlying {@link: arez.ObservableValue} primitive using the {@link: arez.annotations.ObservableValueRef @ObservableValueRef} annotation.
  See [accessing primitives](accessing_primitives.md) documentation on details on how this is achieved.
* Manually invoking {@link: arez.ObservableValue#reportChanged()} in the callback.

For example:

{@file_content: file=arez/doc/examples/at_observable/ReactComponent.java start_line=@ArezComponent "end_line=^}"}
