---
title: @Observe
---

The {@link: arez.annotations.Observe @Observe} annotation is used to define an observe method for an
[observer](observers.md). Observers are typically used for querying arez state and reflecting
that state as side-effects. As such the observe method defaults to being run in a read-only
[transaction](transactions.md) but this can be modified by setting the
{@link: arez.annotations.Observe#mutation() @Observe.mutation} parameter to `true` which will change the
transaction mode to read-write.

An example where an {@link: arez.annotations.Observe @Observe} method can be used is to automatically update
a view when data changes. For example, imagine you wanted to display the value of 1 Bitcoin in
Australian dollars as the value changes over time. Assuming the currency is observable, the
{@link: arez.annotations.Observe @Observe} method may look something like:

{@file_content: file=arez/doc/examples/at_observe/CurrencyView.java start_line=@ArezComponent "end_line=^}"}

This is enough to create a basic web application just using the Arez {@link: arez.annotations.Observe @Observe}
primitive.

## Decoupling the execution and scheduling of the Observer

Sometimes it is useful to decouple the execution of the {@link: arez.annotations.Observe @Observe} method and the
rescheduling of the method when a dependency change is detected. In Arez, this is possible when you pair
the {@link: arez.annotations.Observe @Observe} method with an {@link: arez.annotations.OnDepsChange @OnDepsChange} method. The
{@link: arez.annotations.OnDepsChange @OnDepsChange} is invoked by the Arez runtime when it detects a change has occurred.
It is up to application to invoke {@link: arez.Observer#schedule()} to schedule an
update.

One scenario where this is commonly used is to limit the rate at which an observer reacts. For example
there may not be a need to update the website every time the currency conversion rate changes if it is
changing multiple times a second. It may be sufficient to update the value once every 2 seconds and this
could be achieved with code such as:

{@file_content: file=arez/doc/examples/at_observe2/CurrencyView.java start_line=@ArezComponent}

## Allowing the application to execute observe methods

The [observers](observers.md) documentation describes "tracker" observers as those where the application
is responsible for executing the observe method. This is useful when you need to integrate with other
frameworks that already contain their own scheduler.

This is modelled with a pair of methods; one annotated with {@link: arez.annotations.Observe @Observe} and one
annotated with {@link: arez.annotations.OnDepsChange @OnDepsChange}. In addition it is necessary that the
{@link: arez.annotations.Observe#executor() @Observe.executor} parameter on the {@link: arez.annotations.Observe @Observe}
annotation is set to {@link: arez.annotations.Executor.APPLICATION}.

This approach is used in libraries such as [React4j](https://react4j.github.io) that integrate Arez into
external schedulers. An example that demonstrates something similar is:

{@file_content: file=arez/doc/examples/at_observe3/CurrencyView.java start_line=@ArezComponent "end_line=^}"}
