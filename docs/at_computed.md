---
title: @Computed
---

The {@api_url: annotations.Computed} annotation is used to define [computable values](computable_values.md) within
the component model. The core idea is that you define a method that accepts no parameters and returns a value.
This method is observable and the observer that calls the computable value will be re-scheduled any time that the
computable value changes. Typically methods annotated with the {@api_url: annotations.Computed} annotation are
expected to derive the return value from other [`@Observable`](at_observable.md) and `@Computed` annotated methods.

A basic example:

{@file_content: file=arez/doc/examples/at_computed/CurrencyCollection.java start_line=@ArezComponent "end_line=^}"}

The component model supports the definition of callback methods as described in the
[computable values](computable_values.md) document. This is done through the annotations:
{@api_url: annotations.OnActivate}, {@api_url: annotations.OnDeactivate} and {@api_url: annotations.OnStale}.
These methods are associated with a `@Computed` annotated method via naming conventions or through explicit
configuration. The exact requirements for methods annotated by these annotations is defined in the API documentation.

A simplistic example that uses the callbacks to register a listener on the underlying browser objects and
updates the computable value is as follows. This is extracted and simplified from an existing component that
manages online state.

{@file_content: file=arez/doc/examples/at_computed/NetworkStatus.java start_line=@ArezComponent "end_line=^}"}
