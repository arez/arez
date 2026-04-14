---
title: Computable values
---

Computable values are very similar conceptually to formulas in spreadsheets. Computable values are derived
from observable values and/or other computable values. If a dependency is changed then the computable value
will recalculate it's value. If the new value is different from the old value then the computable value is
marked as changed and the change is propagated to observers.

A computable value will only compute a value if it is active. In most circumstances, a computable value is only
active if it is being observed by an observer or it is used by another computable value. If a computable value
is not needed then no computation occurs.

The benefits of computable values can not be underestimated. Computable values help you make your core state
as small as possible. They are highly optimized and should be used wherever possible.

Don't confuse computable values with [observers](observers.md). Both track dependencies and react to changes
in dependencies, but use computable values if you want to produce a value that can be used by other observers
or computable values. Use [observers](observers.md) if you don't want to produce a new value but instead want
to produce an effect such as imperative side effects like logging, making network requests etc.

The automatic suspension of computable values is very convenient. If a computable value is no longer observed
(i.e the UI in which it was used no longer exists), then Arez will no longer recalculate the value. This differs
from observers that will continue to react to changes in the dependencies until the observer is disposed.

Within the Arez system, a computable value is represented by an instance of the {@link: arez.ComputableValue}
class. The {@link: arez.ComputableValue} class contains a cache of the value that is computed and a reference to the
function that computes the value. It provides a method {@link: arez.ComputableValue#get()} to
access the cached value or recalculate the value if it is stale. The {@link: arez.ComputableValue} contains the necessary
logic required to determine staleness and notify dependencies. Internally it uses instances of {@link: arez.ObservableValue}
and {@link: arez.Observer} to achieve this goal.

The {@link: arez.ComputableValue} class is a relatively low-level primitive and users typically use higher level
constructs such as the [@Memoize](at_memoize.md) annotation.

## Callbacks

When the computable values is created, several callback can be supplied that allow the developer to customize
behaviour when certain state transitions occur. These callbacks are typically used if the computable value needs
to interact with platform services to keep the computable value up to date. i.e. In a web browser environment, a
computable value may add an event listener to browser objects so that it can invalidate the cached value in response
to events from the browser.

The callbacks are:

* **onActivate**: This callback is invoked when the computable value goes from having no observers to having 1 or
  more observers. This callback is often used to subscribe to changes from the native platform.
* **onDeactivate**: This callback is invoked when the computable value goes from having 1 or more observers to
  having no observers. This callback is often used to unsubscribe from changes from the native platform.
* **onStale**: This callback is invoked when the computable value is stale and needs to be recomputed. This callback
  is often used to initiate processes to update the computable value.

## Equality Comparison

Every time an activated computable value's dependency is marked as stale, the computable value will attempt to
re-computed the computable value. If the computable value is equal to the cached value then the computable value
will be marked as unchanged and no downstream observers or computable values will be scheduled. Equality is
tested using an {@link: arez.EqualityComparator}. Computable values generated for
{@link: arez.annotations.Memoize @Memoize} methods use the comparator configured by the
annotation and default to {@link: arez.ObjectsEqualsComparator}.

## Error Handling

Exceptions thrown when calculating computable values are caught by the Arez runtime using the same mechanisms that
handle [errors](observers.md#error-handling) in observers. The exception is cached and will be re-thrown if a
transaction attempts to access the computable value.

## Non-Arez Dependencies

In some cases a {@link: arez.ComputableValue} can be derived from non-arez dependencies. In these scenarios it is
expected that the developer will need to explicitly track the non-arez dependency and notify the
{@link: arez.ComputableValue} when the dependency has changed by invoking the method
{@link: arez.ComputableValue#reportPossiblyChanged()}. The
{@link: arez.ComputableValue} will then be marked as possibly changed and will recalculate the value either on next
access or when the Arez scheduler is next invoked. This approach is particularly useful if you are attempting to
integrate with event based systems or event-driven reactive frameworks such as [RxJava](https://github.com/ReactiveX/RxJava).

It should be noted that the {@link: arez.ComputableValue#reportPossiblyChanged()}
can only be invoked if the {@link: arez.ComputableValue} was created with the flag
{@link: arez.ComputableValue.Flags#AREZ_OR_EXTERNAL_DEPENDENCIES}.
