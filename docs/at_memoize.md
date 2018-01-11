---
title: @Memoize
---

[Memoization](https://en.wikipedia.org/wiki/Memoization) is an optimization technique used primarily to speed up
computer programs by storing the results of expensive function calls and returning the cached result when the same
inputs occur again. Arez takes this a step further by making the memoized value observable. The memoized value will
be re-calculated any time a dependency is updated as long as there is at least one observer.

In Arez you can annotate a method that returns a value with the {@api_url: annotations.Memoize} annotation in an
{@api_url: annotations.ArezComponent} annotated classes. Under the covers the code will create a
{@api_url: ComputedValue} instance for each unique combination of parameters to the method. Each time the memoized
method is invoked, it will return the value cached by the {@api_url: ComputedValue} instance unless the
{@api_url: ComputedValue} instance is stale (i.e. a dependency has changed) or the value of the parameters have
changed. If the memoized method is invoked from within an observer, the observer will be rescheduled any time the
memoized value changes.

The {@api_url: annotations.Memoize} annotation has the advantage over [@Computed](at_computed.md) that the
same method can be used from several different locations without creating a separate `@Computed` method for
each different location. An example where this is useful would be if the same data appears in multiple locations
in the user interface but each separate location is filterered using different criteria.

A basic example where the result of filtering a model can be memoized is as follows.

{@file_content: file=arez/doc/examples/memoize/PersonModel.java start_line=@Observable "end_line=^}" strip_block=true include_end_line=false}
