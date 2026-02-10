---
title: @Memoize
---

[Memoization](https://en.wikipedia.org/wiki/Memoization) is an optimization technique primarily used to speed up
computer programs by storing the results of expensive function calls and returning the cached result when the same
inputs occur again. Arez takes this a step further by making the memoized method observable. The memoized method will
be re-calculated any time a dependency is updated as long as there is at least one observer.

To memoize a method within the Arez component model you annotate a method that returns a value with the
{@link: arez.annotations.Memoize @Memoize} annotation in an {@link: arez.annotations.ArezComponent @ArezComponent} annotated class. The
{@link: arez.annotations.Memoize @Memoize} annotated method is expected to derive the return value from any parameters
passed to the method and/or the return values of other {@link: arez.annotations.Observable @Observable} or
{@link: arez.annotations.Memoize @Memoize} annotated properties.

The {@link: arez.annotations.Memoize @Memoize} annotation is implemented using [computable values](computable_values.md).
If the memoized method has no parameters then there is a single {@link: arez.ComputableValue} supporting the method.
If a memoized method has parameters then the runtime will create a {@link: arez.ComputableValue} instance for each
unique combination of parameters to the method. Each time the memoized method is invoked, it will return the value
cached by the {@link: arez.ComputableValue} instance unless the {@link: arez.ComputableValue} instance is stale (i.e.
a dependency has changed) or the value of the parameters have changed. If the memoized method is invoked from within
an observer, the observer will be rescheduled any time the memoized value changes.

A basic example:

{@file_content: file=arez/doc/examples/at_memoize/CurrencyCollection.java start_line=@ArezComponent "end_line=^}"}

A basic example that passes parameters to the memoized method. In this scenario the result of filtering a
model is memoized as follows.

{@file_content: file=arez/doc/examples/memoize/PersonModel.java start_line=@Observable "end_line=^}" strip_block=true include_end_line=false}

## Equality Comparison

Memoized values only propagate changes when the newly computed value is considered different
from the previously cached value. By default this comparison uses
{@link: arez.ObjectsEqualsComparator}. You can customize this by specifying the
`equalityComparator` parameter on {@link: arez.annotations.Memoize @Memoize}.

Common options include {@link: arez.ObjectsDeepEqualsComparator} or a custom
{@link: arez.EqualityComparator} implementation.

## Lifecycle Hooks

The component model also supports the definition of callback methods as described in the
[computable values](computable_values.md) document. This feature is currently only available if the memoized method
accepts zero parameters. The callbacks are defined by using the annotations {@link: arez.annotations.OnActivate @OnActivate}
and {@link: arez.annotations.OnDeactivate @OnDeactivate}. These methods are associated with the
{@link: arez.annotations.Memoize @Memoize} annotated method via naming conventions or through explicit configuration. The
exact requirements for methods annotated by these annotations is defined in the API documentation.

A simplistic example that uses the callbacks to register a listener on the underlying browser objects and
updates the computable value is as follows. This is extracted and simplified from an existing component that
manages online state.

{@file_content: file=arez/doc/examples/at_memoize/NetworkStatus.java start_line=@ArezComponent "end_line=^}"}

## Explicitly causing Memoized method to re-evaluate

The previous example may have seemed overly complex as and inefficient as we listen to changes from the browser
and reflect the state of the browser in an observable `RawOnLine` just so a memoized method will be informed that
a dependency has changed and recompute the value to see if `OnLine` has changed.

Rather than synthesizing observables just to drive memoized methods it is possible to explicitly trigger a
recalculation of the memoized value. To do this the `depType` parameter of the `@Memoize` annotation must be set
to `AREZ_OR_EXTERNAL` and the application needs to explicitly invoke `reportPossiblyChanged()` on the associated
{@link: arez.ComputableValue}.

For example, the above example could be rewritten more efficiently as:

{@file_content: file=arez/doc/examples/at_memoize2/NetworkStatus.java start_line=@ArezComponent "end_line=^}"}
