---
title: Computed Values
---

Computed values are very similar conceptually to formulas in spreadsheets. Computed values are derived
from observable values and/or other computed values. If a dependency is changed then the computed value
will recalculate it's value. If the new value is different from the old value then the computed value is
marked as changed and the change is propagated to observers.

A computed value will only compute a value if it is active. In most circumstances, a computed value is only
active if it is being observed by an observer or it is used by another computed value. If a computed value
is not needed then no computation occurs.

The benefits of computed values can not be underestimated. Computed values help you make your core state
as small as possible. They are highly optimized and should be used wherever possible.

Don't confuse computed values with [observers](observers.md). Both track dependencies and react to changes
in dependencies, but use computed values if you want to produce a value that can be used by other observers
or computed values. Use [observers](observers.md) if you don't want to produce a new value but instead want
to produce an effect such as imperative side effects like logging, making network requests etc.

The automatic suspension of computed values is very convenient. If a computed value is no longer observed
(i.e the UI in which it was used no longer exists), then Arez will no longer recalculate the value. This differs
from observers that will continue to react to changes in the dependencies until the observer is disposed.

Within the Arez system, a computed value is represented by an instance of the {@api_url: ComputedValue}
class. The {@api_url: ComputedValue} class contains a cache of the value that is computed and a reference to the
function that computes the value. It provides a method {@api_url: ComputedValue.get()::ComputedValue::get()} to
access the cached value or recalculate the value if it is stale. The {@api_url: ComputedValue} contains the necessary
logic required to determine staleness and notify dependencies. Internally it uses instances of {@api_url: ObservableValue}
and {@api_url: Observer} to achieve this goal.

The {@api_url: ComputedValue} class is a relatively low-level primitive and users typically use higher level
constructs such as the [@Computed](at_computed.md) annotation.

## Callbacks

When the computed values is created, several callback can be supplied that allow the developer to customize
behaviour when certain state transitions occur. These callbacks are typically used if the computed value needs
to interact with platform services to keep the computed value up to date. i.e. In a web browser environment, a
computed value may add an event listener to browser objects so that it can invalidate the cached value in response
to events from the browser.

The callbacks are:

* **onActivate**: This callback is invoked when the computed value goes from having no observers to having 1 or
  more observers. This callback is often used to subscribe to changes from the native platform.
* **onDeactivate**: This callback is invoked when the computed value goes from having 1 or more observers to
  having no observers. This callback is often used to unsubscribe from changes from the native platform.
* **onStale**: This callback is invoked when the computed value is stale and needs to be recomputed. This callback
  is often used to initiate processes to update the computed value.

## Equality Comparison

Every time an activated computed value's dependency is marked as stale, the computed value will attempt to
re-computed the computed value. If the computed value is equal to the cached value then the computed value
will be marked as unchanged and no downstream observers or computed values will be scheduled. Equality is
tested using `Object.equals(...)` method.

## Error Handling

Exceptions thrown when calculating computed values are caught by the Arez runtime using the same mechanisms that
handle [errors](observers.md#error-handling) in observers. The exception is cached and will be re-thrown if a
transaction attempts to access the computed value.

## Non-Arez Dependencies

In some cases a {@api_url: ComputedValue} can be derived from non-arez dependencies. In these scenarios it is
expected that the developer will need to explicitly track the non-arez dependency and notify the
{@api_url: ComputedValue} when the dependency has changed by invoking the method
{@api_url: ComputedValue.reportPossiblyChanged()::ComputedValue::reportPossiblyChanged()}. The {@api_url: ComputedValue}
will then be marked as possibly changed and will recalculate the value either on next access or when the
Arez scheduler is next invoked. This approach is particularly useful if you are attempting to integrate with
event based systems or event-driven reactive frameworks such as [RxJava](https://github.com/ReactiveX/RxJava).

It should be noted that the {@api_url: ComputedValue.reportPossiblyChanged()::ComputedValue::reportPossiblyChanged()}
can only be invoked if the {@api_url: ComputedValue} was created with the flag
{@api_url: Flags.NON_AREZ_DEPENDENCIES::Flags::NON_AREZ_DEPENDENCIES}.
