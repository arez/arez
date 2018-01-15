---
title: @Observable
---

The {@api_url: annotations.Observable} annotation simplifies writing [observable](observables.md) properties.
The simplest use-case involves annotating one of a standard getter/setter pair with the annotation.

For example:

{@file_content: file=arez/doc/examples/at_observable/MyModel.java start_line=@ArezComponent "end_line=^}"}

There are additional constraints that are apply when defining observable. i.e. The type of the getter and setter
must match. These constraints are detailed in the {@api_url: annotations.Observable} API documentation.

## Customizing names

The `@Observable` annotation methods can be named using non-standard naming conventions and this is sometimes
required to integrate with existing frameworks. In this scenario, the user must add and additional parameter
to the annotation to explicitly name the observable property. This may require annotating both the setter and
the setter with the `@Observable` annotation.

For example:

{@file_content: file=arez/doc/examples/at_observable/Currency.java start_line=@ArezComponent "end_line=^}"}

## Custom change propagation

Sometimes you want to integrate Arez into systems that have existing change propagation mechanisms. For
example, Arez was integrated in a java variant of React called [React4j](https://react4j.github.io). React4j
components are configured by passing the component `props`. These components can be re-configured by passing
the component another value for `props`. The react framework is wholly responsible for detecting changes in
`props`, assigning the `props` field and then invoking a callback method on the component to notify the
component that `props` has changed. Making this `props` property observable within Arez involved the following
changes.

* Disabling the `@Observable` setter via the {@api_url: Observable.expectSetter()::annotations.Observable::expectSetter()}
  parameter on the getter.
* Accessing the underlying {@api_url: Observable} primitive using the {@api_url: annotations.ObservableRef} annotation.
  See [accessing primitives](accessing_primitives.md) documentation on details on how this is achieved.
* Manually invoking {@api_url: Observable.reportChanged()::Observable::reportChanged()} in the callback.

For example:

{@file_content: file=arez/doc/examples/at_observable/ReactComponent.java start_line=@ArezComponent "end_line=^}"}
