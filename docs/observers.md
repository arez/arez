---
title: Observers
---

Observers are the elements within an Arez application that react to changes. Observers are typically about
initiating effects. Each observer is associated with an `observed` function. When the function executes, Arez
tracks which [observable values](observable_values.md) and [computable values](computable_values.md) are accessed
within the scope of the function and these elements are recorded as dependencies of the observer. Any
time a dependency is changed, Arez will invoke a hook function that will ultimately result in scheduling
the observer to re-execute the observed function. The {@api_url: Observer} class represents the observer within
the Arez toolkit.

The observed function can either be invoked by the Arez runtime or by the host application. Using Arez as the
executor is the default choice but sometimes it is useful to designate the application as the executor.
Application invocation of observed functions is useful when Arez needs to integrate into other scheduler
frameworks. In most cases that use an external executor, Arez pushes change notifications to a logical queue
and the external framework pulls change notifications during their own update phase. For example, the
[React4j](https://react4j.github.io) library uses an external executor so that Arez can be integrated into
reacts scheduler.

An application can provide an `onDepsChange` hook function when creating the observer and Arez will invoke
the hook method when Arez detects that a dependency has changed. If the observer needs to support application
executor then the hook function **must** be provided. If the hook function is provided but the observed function
is not expected to be invoked by the application, then the application code must invoke the
{@api_url: Observer.schedule()::Observer::schedule()} method either in the hook method or at some later time.
This will schedule the {@api_url: Observer} so that the observed function is invoked by the Arez runtime
next time that the Arez scheduler is triggered.

If an observer is created without a `onDepsChange` hook function then Arez will automatically defines an
`onDepsChange` function that immediately reschedules the observer. Other reactive frameworks often refer
to this type of an observer as an "autorun" observer.

## API

There are several low-level {@api_url: ArezContext.observer(*)::ArezContext::observer(arez.Procedure)}
methods that can be used to create observers, however most users will use more high-level APIs such as
the [@Observe](at_observe.md) annotation.

An example of a basic observer:

{@file_content: file=arez/doc/examples/observe/ObserverExample.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}

An example of an observer that is explicitly named and uses a read-write transaction:

{@file_content: file=arez/doc/examples/observe/ObserverExample2.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}

A "tracker" observer is created with a `onDepsChange` hook function but no `observed` function. i.e. A
tracker observer uses an application executor. Using a tracker observer is a little more complex within Arez.
The developer must explicitly create the observer via {@api_url: ArezContext.tracker(*)::ArezContext::tracker(arez.Procedure)}
invocation and then explicitly observe the observed function via {@api_url: ArezContext.observe(*)::ArezContext::observe(arez.Observer,arez.Procedure)}.

A very simple example of a tracker observer:

{@file_content: file=arez/doc/examples/tracker/TrackerExample.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}

## Error Handling

If an observer's `observed` function is executed by the Arez runtime and throws an exception, then that exception
is caught by the Arez scheduler and passed to an error handler. The same occurs if an exception is thrown invoking
an observers `onDepsChange` function. This is to make sure that an exception in one observer does not
prevent the scheduled execution of other observers. This also allows observers to recover from exceptions; throwing
an exception does not break the tracking done by Arez, so a subsequent scheduling of an observer will complete
normally again if the cause for the exception is removed.

The error handlers are added and removed from the `ArezContext` by way of the {@api_url: ArezContext.addObserverErrorHandler(ObserverErrorHandler)::ArezContext::addObserverErrorHandler(arez.ObserverErrorHandler)}
and {@api_url: ArezContext.removeObserverErrorHandler(ObserverErrorHandler)::ArezContext::removeObserverErrorHandler(arez.ObserverErrorHandler)}
methods. A simple example that emits errors to the browsers console in a browser environment follows:

{@file_content: file=arez/doc/examples/observer_error/ObserverErrorHandlerExample.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}
