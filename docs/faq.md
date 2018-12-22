---
title: Frequently Asked Questions
---
<nav class="page-toc">

<!-- toc -->

- [Application Development](#application-development)
  * [Why are @Observe methods not being re-run when observable properties change?](#why-are-observe-methods-not-being-re-run-when-observable-properties-change)
- [Library Design](#library-design)
  * [Why do the change events/notifications not include a description of the change?](#why-do-the-change-eventsnotifications-not-include-a-description-of-the-change)
- [Component Model](#component-model)
  * [Why do the generated/enhanced Arez components implement Disposable?](#why-do-the-generatedenhanced-arez-components-implement-disposable)
  * [Why does the annotation processor override equals() and hashCode() methods?](#why-does-the-annotation-processor-override-equals-and-hashcode-methods)
  * [Why does the annotation processor only sometimes generate a toString() method?](#why-does-the-annotation-processor-only-sometimes-generate-a-tostring-method)
- [Library Implementation Decisions](#library-implementation-decisions)
  * [Why guard invariant checks with `Arez.shouldCheckInvariants()`?](#why-guard-invariant-checks-with-arezshouldcheckinvariants)
- [Alternatives](#alternatives)
  * [How does Arez compare to Mobx?](#how-does-arez-compare-to-mobx)
  * [How does Arez compare to Incremental?](#how-does-arez-compare-to-incremental)

<!-- tocstop -->

</nav>

## Application Development

### Why are @Observe methods not being re-run when observable properties change?

Arez only re-runs {@api_url: annotations.Observe} annotated methods if it is told that an observable property that
is a dependency of the {@api_url: annotations.Observe} method is changed. Assuming you are using classes annotated
with {@api_url: annotations.ArezComponent} then this means that the code must:

* mutate the property using the setter method to mark the property as changed. The generated code ultimately calls
  the {@api_url: ObservableValue.reportChanged()::ObservableValue::reportChanged()} method to mark the property as changed.
* access the property using the getter method.. The generated code ultimately calls the
  {@api_url: ObservableValue.reportObserved()::ObservableValue::reportObserved()} method to mark the property as observed.
  This will add it as a dependency to the containing `@Observe` method.

Typically this problem arises when you mutate field directly within the same class. Consider this problematic code
snippet:

```java
  @Observable
  public int getRemainingRides()
  {
    return _remainingRides;
  }

  public void setRemainingRides( int remainingRides )
  {
    _remainingRides = remainingRides;
  }

  @Action
  public void rideTrain()
  {
    _remainingRides = _remainingRides - 1;
  }
```

Compare it to this code that correctly notifies observers that the `remainingRides` property has updated. The only
difference is how the state is mutated within `rideTrain()` method.

```java
  @Observable
  public int getRemainingRides()
  {
    return _remainingRides;
  }

  public void setRemainingRides( int remainingRides )
  {
    _remainingRides = remainingRides;
  }

  @Action
  public void rideTrain()
  {
    setRemainingRides( getRemainingRides() - 1 );
  }
```

## Library Design

### Why do the change events/notifications not include a description of the change?

In many state management frameworks, notifications of change are accompanied by a description
of change but not so in Arez. For example in Arez the {@api_url: ObservableValue.reportChanged()::ObservableValue::reportChanged()}
method accepts no change description, the {@api_url: spy.ObservableValueChangeEvent} class
has no description of a change and there is no way for an {@api_url: Observer} to receive changes.

The reason is that change descriptions seem to be used as an optimization strategy needed in very specific
scenarios. For example, consider the scenario where you are representing an `Employee` as a react component
and you maintain a list of `Employee` instances that are candidates for a particular allowance. Calculating the
applicability of an allowance for a particular `Employee` is an expensive operation. In Arez you are forced to
regenerate the list of allowance candidates each time the set of `Employee` instances changes. In other
systems you could just listen for an event such as `EntityAdded(Employee)`, calculate the applicability for that
specific instance and insert them into the allowance candidates if appropriate. In that scenario the event
listen approach can be better optimized.

However building this in Arez with judicious use of {@api_url: annotations.Memoize} annotated
methods can achieve acceptable performance, at least in our tests of up to ~8000 entities in the original `Employee`
set. It is possible that in the future, change messages will be added back into Arez but will only occur when
it is determined that the implementation and performance costs for other scenarios is worth the tradeoff.

This decision was not made lightly and the original Arez implementation included change events such as
`AtomicChange(FromValue, ToValue)`, `MapAdd(Key, Value)`, `Disposed()`, `UnspecifiedChange()` etc. These events
had to be queued on the `Observer` in the order they were generated and explicitly consumed by the `Observer`
during the reaction phase. This added some complexity to the Arez implementation. The author of Arez has also
previously implemented two other state management frameworks using this technique and considers the complexity
for downstream consumers to be the greatest problem with this strategy.

## Component Model

### Why do the generated/enhanced Arez components implement Disposable?

The enhanced component classes generated by the annotation processor all implement the {@api_url: Disposable}. This
makes it possible to explicitly decommission the reactive component and release all the Arez resources
associated with the component. A previous iteration of the framework made it optional for components to
support {@api_url: Disposable} but this just lead to resource leaks.

It should also be noted that all resources within a component are disposed within the scope of a single
transaction, to avoid scenario where a partially disposed component reacts to changes occurring during
dispose.

### Why does the annotation processor override equals() and hashCode() methods?

The annotation processor overrides the `equals()` and `hashCode()` methods as these methods
are used internally by Arez when storing instances of these classes in a repository is generated or the
`Arez.areNativeComponentsEnabled()` method returns true. The methods are implemented with
the assumption that the component id is unique. If the component id is supplied by the toolkit user via
the {@api_url: annotations.ComponentId} annotation, the user must be careful to ensure that the component
id is unique. It should be noted that the user may more explicitly control the generation of these
methods using the `requireEquals` parameter on the {@api_url: annotations.ArezComponent} annotation.

### Why does the annotation processor only sometimes generate a toString() method?

The Arez annotation processor will generate a `toString()` method on a component if the user has not provided
their own `toString()` method. If any superclass other than the base `Object` class has overridden the `toString()`
method, then Arez assumes that the user will want to keep that method. If the method has not been overridden
then Arez will override `toString()` to return a value such as `"ArezComponent[myComponentName]"`. It should be
noted that if names are not enabled by the Arez compile time configuration (i.e.
{@api_url: Arez.areNamesEnabled()::Arez::areNamesEnabled()} returns false) then the base `Object.toString()`
method will be invoked.

## Library Implementation Decisions

### Why guard invariant checks with `Arez.shouldCheckInvariants()`?

The codebase is filled with lots of calls to the `Guards.invariant(...)` and `Guards.apiInvariant(...)` methods
from the `BrainCheck` library. If the appropriate configuration settings are supplied, these methods will verify
that invariants and expectations of the Arez library are true at runtime. As these checks can be expensive, they
should not be run in production mode. If these checks are not run then the code should not be generated in a
GWT/Browser based application.

The initial design of `BrainCheck` went through several iterations to ensure that the code is not present in
generated javascript output and this seems to be true when the code complexity is low. However the GWT 2.8.2
compiler will fail to eliminate the code for the lambdas passed to the the invariant methods, even after the
code for invariant methods is eliminated. It is unclear the trigger for this problem as the same sequence of
code would be optimized in one context but kept in another context within the same application.

To work around this limitation of the GWT compiler, code the previously looked like:

    Guards.invariant( () -> this.state == expected,
                      () -> "State " + this.state + " does not match expected " + expected );

Had to be rewritten to look like:

    if ( Arez.shouldCheckInvariants() )
    {
      Guards.invariant( () -> this.state == expected,
                        () -> "State " + this.state + " does not match expected " + expected );
    }

This has produced significantly more clutter in the codebase but needs to be supported while GWT 2.x continues
to be a deployment target. As an indication of how much of an impact it can have, the size of one trivial
application went from 141KB to 74KB after this change. If/When Arez targets GWT 3.x and not GWT 2.x, it is
expected that most of this complexity can be removed from the codebase.

## Alternatives

### How does Arez compare to Mobx?

Arez began life as a port of Mobx so they both share a core conceptual model of observable values, computable
values (a.k.a derived values) and observers. The internals that implement Arez are better optimized for both
execution speed and base code size than the Mobx equivalents due to the magic of the GWT2.x and J2CL compilers.
For many use cases Arez has a much smaller memory footprint.

As the number of observable nodes increases, Mobx has the advantage and the code size will increase at a
lower rate compared to a normal Arez application. The actual threshold where the size of the Arez code is higher
than the equivalent functionality in Mobx is a moving target as both projects are continually evolving. In
practice it is expected to be in the vicinity of ~6000 `@Observable` annotated methods but this has not been
measured recently.

The reason for the difference is primarily how the two different frameworks make an object reactive. The Arez
component model statically generates code at compile time to make an element reactive while Mobx introduces
reactivity dynamically at runtime. Arez has experimented with using dynamic runtime enhancement of a component
but this approach relied upon features of J2CL and J2CL is still in development. Arez's dynamic components were
also limited to running on a javascript virtual machine and would no longer be able to be run on a JVM without
an alternative implementation strategy.

Mobx has a much larger ecosystem with a larger developer base. As a result Mobx offers much better educational
resources (such as documentation and video courses). Some features of Mobx (i.e. `when(...)`) are only available
as libraries (i.e. `arez-when` project) where they are baked into Mobx. Arez includes an opinionated
component model within the core framework where as the equivalent within in the Mobx ecosystem is provided by
[Mobx State Tree](https://github.com/mobxjs/mobx-state-tree) or MST. MST provides similar features to the Arez
component model as well as additional functionality such as serialization and deserialization of state.

Arez includes a significantly more advanced scheduler where observers and memoized functions can be scheduled at
different priorities. The scheduler also allows user code to explicitly schedule tasks that will be interleaved
with observer reactions. Both of these features have opened up significant mechanisms for improving performance

Overall the two libraries share many similarities. Arez is focused on performance and is written in Java. Mobx
has a larger ecosystem and is written in javascript (or TypeScript to be more precise).

### How does Arez compare to Incremental?

[Incremental](https://github.com/janestreet/incremental) is an Ocaml library that should have inspired Arez
but it was not discovered until after Arez had crystalized. Many of the core elements in Incremental have direct
parallels in Arez. i.e. Incremental's concepts of `variable`, `incremental` and `observer` are approximately
equivalent to Arez's {@api_url: ObservableValue}, {@api_url: ComputableValue} and {@api_url: Observer}.

Incremental differs in that it manually triggers scheduling of tasks to converge the state after a variable has
changed via a stabilize call. This is in contrast with Arez that automatically converges the state after every
{@api_url: ObservableValue} change unless the develoepr has specifically paused the scheduler. Incremental also
assumes a directed acyclic graph where Arez assumes an arbitrary graph that will eventually stabilize.

Incremental is based upon the [Self-Adjusting Computation](http://www.umut-acar.org/self-adjusting-computation)
academic literature which should be considered a significant advantage. Arez (and Mobx before it) has stumbled
on the same techniques as outlined in the research literature (i.e. Dynamic dependence graphs and memoized dynamic
dependence graphs as a way of implementing change propagation) but both lack the rigour that is present in the
literature. The author is unable to determine whether the Incremental implementation has the precision described
in the literature as the library was primarily assessed based on the [technical documentation](https://github.com/janestreet/incremental/blob/master/src/incremental_intf.ml),
the initial [announcement](https://blog.janestreet.com/introducing-incremental) and a [conference talk](https://www.youtube.com/watch?v=HNiFiLVg20k).

Incremental also provides a "... low-level, experimental interface to incremental" which allows applications
to explicitly control change propagation for performance reasons. It comes at the cost that it's much harder
to use right. The capabilities offered by this interface include:

* Receive notification when a dependency changes so that the `"expert"` node can update itself incrementally.
* Allow the `"expert"` node to update the nodes that depend upon it.
* Allow the `"expert"` node to select the nodes that depend upon it that will react to changes.

While this approach is fraught with danger and highly problematic, if done correctly it can significantly
improve performance. Imagine you have an observable property that identifies the UI component that is
selected by the user and there is 1000 UI components that have a memoized boolean property `"isSelected"`
that indicates whether they are selected. When the selected value is changed, both Arez and Mobx would
schedule 1000 reactions which would ultimately result in two changes (i.e. one UI component's `"isSelected"`
property becoming `true` and one UI component's `"isSelected"` property becoming `false`). Incremental
allows for the possibility that a custom `"expert"` node for the `"selected"` property could instead just
trigger two reactions, thus avoiding 998 wasted reactions.

Incremental also allows incremental updates which is commonly used when interacting with an imperative API.
VirtualDOM is like this. Compute the desired state, then perform diff against last state and perform patching
against actual DOM to align. So get two variables (before VDOM, after VDOM) and use diff and patch operations
to apply effects. This could be implemented within Incremental whereas both Arez and Mobx defer to other frameworks
such as react to achieve a similar goal.

Incremental also has some downsides when compared to Arez as a cycle in the dependence graph or an exception
during stabilization will cause the stabilization process to terminate. Cycles are explicitly supported within
Arez as long as the system stabilizes within a fixed number of rounds. An exception within Arez can be handled
locally but even if unhandled, it will only stop change propagation within the graph that the dependency exists
and it will recover gracefully if the condition that caused the error is resolved.
