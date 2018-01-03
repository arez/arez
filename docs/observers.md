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

## Tracker Observers

Tracker observers separate the tracked function from the callback that is scheduled when the dependencies
change. This type of observer is used when you need to integrate into a framework that has it's own scheduler
or when you need to take more control of the scheduling of observers (i.e. to debounce changes or limit the
invocation of the tracked function to at most once per second).
