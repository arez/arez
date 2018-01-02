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
access the cache value or recalculate the value if it is stale. The {@api_url: ComputedValue} contains the necessary
logic required to determine staleness and notify dependencies. Internally it uses instances of {@api_url: Observable}
and {@api_url: Observer} to achieve this goal.
