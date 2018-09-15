---
title: @Observed
---

The {@api_url: annotations.Observed} annotation is used to define an observed method for an
[observer](observers.md). Observers are typically used for querying arez state and reflecting
that state as side-effects. As such the observed method defaults to being run in a read-only
[transaction](transactions.md) but this can be modified by setting the
{@api_url: mutation::annotations.Observed::mutation()} parameter to `true` which will change the
transaction mode to read-write.

An example where an {@api_url: annotations.Observed} method can be used is to automatically update
a view when data changes. For example, imagine you wanted to display the value of 1 Bitcoin in
Australian dollars as the value changes over time. Assuming the currency is observable, the
{@api_url: annotations.Observed} method may look something like:

{@file_content: file=arez/doc/examples/at_observed/CurrencyView.java start_line=@ArezComponent "end_line=^}"}

This is enough to create a basic web application just using the Arez {@api_url: annotations.Observed}
primitive.

## Decoupling the execution and scheduling of the Observer

Sometimes it is useful to decouple the execution of the {@api_url: annotations.Observed} method and the
rescheduling of the method when a dependency change is detected. In Arez, this is possible when you pair
the {@api_url: annotations.Observed} method with an {@api_url: annotations.OnDepsChanged} method. The
{@api_url: annotations.OnDepsChanged} is invoked by the Arez runtime when it detects a change has occurred.
It is up to application to invoke {@api_url: Observer.schedule()::Observer::schedule()} to schedule an
update.

One scenario where this is commonly used is to limit the rate at which an observer reacts. For example
there may not be a need to update the website every time the currency conversion rate changes if it is
changing multiple times a second. It may be sufficient to update the value once every 2 seconds and this
could be achieved with code such as:

{@file_content: file=arez/doc/examples/at_observed2/CurrencyView.java start_line=@ArezComponent}

## Allowing the application to execute observed methods

The [observers](observers.md) documentation describes "tracker" observers as those where the application
is responsible for executing the observed method. This is useful when you need to integrate with other
frameworks that already contain their own scheduler.

This is modelled with a pair of methods; one annotated with {@api_url: annotations.Observed} and one
annotated with {@api_url: annotations.OnDepsChanged}. In addition it is necessary that the
{@api_url: executor::annotations.Observed::executor()} parameter on the {@api_url: annotations.Observed}
annotation is set to {@api_url: Executor.APPLICATION::annotations.Executor::APPLICATION}.

This approach is used in libraries such as [React4j](https://react4j.github.io) that integrate Arez into
external schedulers. An example that demonstrates something similar is:

{@file_content: file=arez/doc/examples/at_observed3/CurrencyView.java start_line=@ArezComponent "end_line=^}"}
