---
title: Frequently Asked Questions
category: General
order: 1
toc: true
---

### Design

#### Why do the change events/notifications not include a description of the change?

In many state management frameworks, notifications of change are accompanied by a description
of change but not so in Arez. For example in Arez the [Observable.reportChanged()]({% api_url Observable reportChanged() %})
method accepts no change description, the [ObservableChangedEvent]({% api_url spy.ObservableChangedEvent %}) class
has no description of a change and there is no way for an [Observer]({% api_url Observer %}) to receive changes.

The reason is that change descriptions seem to be used as an optimization strategy needed in very specific
scenarios. For example, consider the scenario where you are representing an `Employee` as a react component
and you maintain a list of `Employee` instances that are candidates for a particular allowance. Calculating the
applicability of an allowance for a particular `Employee` is an expensive operation. In Arez you are forced to
regenerate the list of allowance candidates each time the set of `Employee` instances changes. In other
systems you could just listen for an event such as `EntityAdded(Employee)`, calculate the applicability for that
specific instance and insert them into the allowance candidates if appropriate. In that scenario the event
listen approach can be better optimized.

However building this in Arez with judicious use of [@Computed]({% api_url annotations.Computed %}) annotated
methods can achieve acceptable performance, at least in our tests of up to ~8000 entities in the original `Employee`
set. It is possible that in the future, change messages will be added back into Arez but will only occur when
it is determined that the implementation and performance costs for other scenarios is worth the tradeoff.

This decision was not made lightly and the original Arez implementation included change events such as
`AtomicChange(FromValue, ToValue)`, `MapAdd(Key, Value)`, `Disposed()`, `UnspecifiedChange()` etc. These events
had to be queued on the `Observer` in the order they were generated and explicitly consumed by the `Observer`
during the reaction phase. This added some complexity to the Arez implementation. The author of Arez has also
previously implemented two other state management frameworks using this technique and considers the complexity
for downstream consumers to be the greatest problem with this strategy.
