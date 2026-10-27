---
title: Observers
---

Observers are the elements within an Arez application that react to changes. Observers are typically about
initiating effects. Each observer is associated with an `observed` function. When the function executes, Arez
tracks which [observable values](observable_values.md) and [computable values](computable_values.md) are accessed
within the scope of the function and these elements are recorded as dependencies of the observer. Any
time a dependency is changed, Arez will invoke a hook function that will ultimately result in scheduling
the observer to re-execute the observed function. The {@link: arez.Observer} class represents the observer within
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
{@link: arez.Observer#schedule()} method either in the hook method or at some later time.
This will schedule the {@link: arez.Observer} so that the observed function is invoked by the Arez runtime
next time that the Arez scheduler is triggered.

If an observer is created without a `onDepsChange` hook function then Arez will automatically defines an
`onDepsChange` function that immediately reschedules the observer. Other reactive frameworks often refer
to this type of an observer as an "autorun" observer.

## API

There are several low-level {@link: arez.ArezContext#observer(arez.Procedure) ArezContext.observer(*)}
methods that can be used to create observers, however most users will use more high-level APIs such as
the [@Observe](at_observe.md) annotation.

An example of a basic observer:

{@file_content: file=arez/doc/examples/observe/ObserverExample.java "start_line=public static void main" "end_line=^    }" include_start_line=false include_end_line=false strip_block=true}

An example of an observer that is explicitly named and uses a read-write transaction:

{@file_content: file=arez/doc/examples/observe/ObserverExample2.java "start_line=public static void main" "end_line=^    }" include_start_line=false include_end_line=false strip_block=true}

A "tracker" observer is created with a `onDepsChange` hook function but no `observed` function. i.e. A
tracker observer uses an application executor. Using a tracker observer is a little more complex within Arez.
The developer must explicitly create the observer via {@link: arez.ArezContext#tracker(arez.Procedure) ArezContext.tracker(*)}
invocation and then explicitly observe the observed function via {@link: arez.ArezContext#observe(arez.Observer,arez.Procedure) ArezContext.observe(*)}.

A very simple example of a tracker observer:

{@file_content: file=arez/doc/examples/tracker/TrackerExample.java "start_line=public static void main" "end_line=^    }" include_start_line=false include_end_line=false strip_block=true}

## Suspending an observer

Use {@link: arez.Observer#suspend() Observer.suspend()} to temporarily stop an observer's automatic reactions
and release its dependencies and registered hooks. The method returns a {@link: arez.Disposable} handle.
Each call creates an independent suspension; the observer resumes only after every handle has been disposed.
Disposing a handle repeatedly is harmless.

For an annotated component, obtain the observer through an
{@link: arez.annotations.ObserverRef @ObserverRef} method:

```java
@ObserverRef
abstract Observer getRenderObserver();

// Suspend rendering until this handle is disposed.
final Disposable suspension = getRenderObserver().suspend();
// Later:
suspension.dispose();
```

Suspension suppresses pending reactions and notifications, including explicit
{@link: arez.Observer#schedule()} and {@link: arez.Observer#reportStale()} requests. An observation already
in progress finishes before its dependencies and hooks are released. Upstream computable values can then
deactivate when no other observer or keep-alive handle needs them.

Explicit observations through an external executor still execute while suspended and preserve their return
values and exceptions. They use temporary tracking: dependencies and hooks are released when the observation
completes, including when it throws. They can temporarily acquire resources needed by the observation, but
their results do not keep those resources alive. In particular, components configured with
{@link: arez.annotations.ArezComponent#disposeOnDeactivate() disposeOnDeactivate = true} can be disposed
after a temporary observation; an external caller must not rely on them remaining alive in its returned result.
Suspension also cannot cancel work already handed to an external scheduler.

Releasing the last suspension schedules one fresh observation for an internal executor. For an external
executor, it schedules one `onDepsChange` notification so the host can arrange a fresh observation and restore
persistent tracking. This refresh is required even if no dependency is known to have changed, since the
observer was detached. Intermediate changes are not replayed. Resumption follows normal transaction,
scheduler-lock and priority rules and can run before the handle's `dispose()` call returns.

Suspension applies to ordinary observers, not observers owned by computable values. Disposing an observer
remains terminal: releasing outstanding suspension handles afterward does not reactivate it. Calling
`suspend()` on a disposed observer is invalid.

## Error Handling

If an observer's `observed` function is executed by the Arez runtime and throws an exception, then that exception
is caught by the Arez scheduler and passed to an error handler. The same occurs if an exception is thrown invoking
an observers `onDepsChange` function. This is to make sure that an exception in one observer does not
prevent the scheduled execution of other observers. This also allows observers to recover from exceptions; throwing
an exception does not break the tracking done by Arez, so a subsequent scheduling of an observer will complete
normally again if the cause for the exception is removed.

The error handlers are added and removed from the `ArezContext` by way of the {@link: arez.ArezContext#addObserverErrorHandler(arez.ObserverErrorHandler) ArezContext.addObserverErrorHandler(ObserverErrorHandler)}
and {@link: arez.ArezContext#removeObserverErrorHandler(arez.ObserverErrorHandler)  ArezContext.removeObserverErrorHandler(ObserverErrorHandler)}
methods. A simple example that emits errors to the browsers console in a browser environment follows:

{@file_content: file=arez/doc/examples/observer_error/ObserverErrorHandlerExample.java "start_line=public static void main" "end_line=^    }" include_start_line=false include_end_line=false strip_block=true}
