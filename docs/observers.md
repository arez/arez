---
title: Observers
---

Observers are the elements within an Arez application that react to changes. Observers are about
initiating effects. Each observer is associated with a tracked function. When the function executes,
Arez tracks which [observables](observables.md) and [computed values](computed_values.md) are accessed
within the scope of the function and these elements are recorded as dependencies of the observer. Any
time a dependency is changed, Arez will re-schedule the observer. There are two types of observer
supported within Arez; **Autorun** observers and **Tracker** observers.

## Autorun Observers

The Arez scheduler is responsible for scheduling the tracked function for autorun observers. Normally when
an autorun observer is defined, the tracked function will be triggered once immediately and then once again
any time the dependencies change. The Arez scheduler is also responsible for wrapping the tracked function in a
tracking transaction.

There are several low-level {@api_url: ArezContext.autorun(*)::ArezContext::autorun(org.realityforge.arez.Procedure)}
methods that can be used to create autorun observers, however most users will use more high-level APIs such as
the [@Autorun](at_autorun.md) annotation.

An example of a basic autorun observer:

{@file_content: file=org/realityforge/arez/doc/examples/autorun/AutorunExample.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}

An example of an autorun observer that is explicitly named, uses a read-only transaction and returns a value:

{@file_content: file=org/realityforge/arez/doc/examples/autorun/AutorunExample2.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}

## Tracker Observers

Tracker observers separate the tracked function from the callback that is scheduled when the dependencies
change. This type of observer is used when you need to integrate into a framework that has it's own scheduler
or when you need to take more control of the scheduling of observers (i.e. to debounce changes or limit the
invocation of the tracked function to at most once per second).

A tracker observer is more complex than autorun observers within Arez. The developer must explicitly create
the observer via {@api_url: ArezContext.tracker(*)::ArezContext::tracker(org.realityforge.arez.Procedure)}
invocation.

{@file_content: file=org/realityforge/arez/doc/examples/tracker/TrackerExample.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}

As with Autorun observers these primitive APIs are unlikely to be directly used by the user but it is more likely
that they will be used indirectly by high-level apis such as the [@Track](at_track.md) annotation.

## Error Handling

Exceptions thrown in an autorun observers's tracked function or a tracker observer's schedule callback are caught by
the Arez scheduler and passed to an error handler. This is to make sure that an exception in one observer does not
prevent the scheduled execution of other observers. This also allows observers to recover from exceptions; throwing
an exception does not break the tracking done by Arez, so a subsequent scheduling of an observer will complete
normally again if the cause for the exception is removed.

The error handlers are added and removed from the `ArezContext` by way of the {@api_url: ArezContext.addObserverErrorHandler(ObserverErrorHandler)::ArezContext::addObserverErrorHandler(org.realityforge.arez.ObserverErrorHandler)}
and {@api_url: ArezContext.removeObserverErrorHandler(ObserverErrorHandler)::ArezContext::removeObserverErrorHandler(org.realityforge.arez.ObserverErrorHandler)}
methods. A simple example that emits errors to the browsers console in a browser environment follows:

{@file_content: file=org/realityforge/arez/doc/examples/observer_error/ObserverErrorHandlerExample.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}
