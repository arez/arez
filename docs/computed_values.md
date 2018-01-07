---
title: Computed Values
---

Computed values are values that can be derived from the existing state or other computed values. Conceptually,
they are very similar to formulas in spreadsheets. Computed values can't be underestimated, as they help you
to make your actual modifiable state as small as possible. Besides that they are highly optimized, so use
them wherever possible.

Computed values are automatically derived from your state if any value that affects them changes. Computed
values can be optimized away in many cases by Arez as they are assumed to be pure. For example, a computed
property won't re-run if none of the data used in the previous computation changed. Nor will a computed
property re-run if is not in use by some other computed property or reaction. In such cases it will be
suspended.

Don't confuse computed values with autorun [observers](observers.md). They are both reactively invoked
expressions, but use computed values if you want to produce a value that can be used by other observers
or computed values and use autorun [observers](observers.md) if you don't want to produce a new value but
rather want to achieve an effect. For example imperative side effects like logging, making network requests
etc.

This automatic suspension is very convenient. If a computed value is no longer observed, for example the
UI in which it was used no longer exists, then Arez wil no longer recalculate the value. This differs from
autorun observer's values where you must dispose of them yourself.

Within the Arez system a computed value observable is represented by an instance of the {@api_url: ComputedValue}
class. The {@api_url: ComputedValue} class contains a cache of the value that is computed and a reference to the
function that computes the value. It provides a method {@api_url: ComputedValue.get()::ComputedValue::get()} to
access the cached value or recalculate the value if it is stale. The {@api_url: ComputedValue} contains the necessary
logic required to determine staleness and notify dependencies. Internally it uses instances of {@api_url: Observable}
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
* **onDispose**: This callback is invoked when the computed value is disposed. This callback is used to release
  resources (i.e. event listeners) that have been allocated in other callbacks.

