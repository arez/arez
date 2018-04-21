---
title: Frequently Asked Questions
---
<nav class="page-toc">

<!-- toc -->

- [Application Development](#application-development)
  * [Why are @Autorun methods not being re-run when observable properties change?](#why-are-autorun-methods-not-being-re-run-when-observable-properties-change)
- [Library Design](#library-design)
  * [Why do the change events/notifications not include a description of the change?](#why-do-the-change-eventsnotifications-not-include-a-description-of-the-change)
- [Component Model](#component-model)
  * [Why do the generated/enhanced Arez components implement Disposable?](#why-do-the-generatedenhanced-arez-components-implement-disposable)
  * [Why does the annotation processor override equals() and hashCode() methods?](#why-does-the-annotation-processor-override-equals-and-hashcode-methods)
  * [Why does the annotation processor only sometimes generate a toString() method?](#why-does-the-annotation-processor-only-sometimes-generate-a-tostring-method)
- [Library Implementation Decisions](#library-implementation-decisions)
  * [Why guard invariant checks with `Arez.shouldCheckInvariants()`?](#why-guard-invariant-checks-with-arezshouldcheckinvariants)

<!-- tocstop -->

</nav>

## Application Development

### Why are @Autorun methods not being re-run when observable properties change?

Arez only re-runs {@api_url: annotations.Autorun} annotated methods if it is told that an observable property that
is a dependency of the {@api_url: annotations.Autorun} method is changed. Assuming you are using classes annotated
with {@api_url: annotations.ArezComponent}  then this means
that the code must:

* mutate the property using the setter method to mark the property as changed. The generated code ultimately calls
  the {@api_url: Observable.reportChanged()::Observable::reportChanged()} method to mark the property as changed.
* access the property using the getter method.. The generated code ultimately calls the
  {@api_url: Observable.reportObserved()::Observable::reportObserved()} method to mark the property as observed.
  This will add it as a dependency to the containing `@Autorun` method.

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
of change but not so in Arez. For example in Arez the {@api_url: Observable.reportChanged()::Observable::reportChanged()}
method accepts no change description, the {@api_url: spy.ObservableChangedEvent} class
has no description of a change and there is no way for an {@api_url: Observer} to receive changes.

The reason is that change descriptions seem to be used as an optimization strategy needed in very specific
scenarios. For example, consider the scenario where you are representing an `Employee` as a react component
and you maintain a list of `Employee` instances that are candidates for a particular allowance. Calculating the
applicability of an allowance for a particular `Employee` is an expensive operation. In Arez you are forced to
regenerate the list of allowance candidates each time the set of `Employee` instances changes. In other
systems you could just listen for an event such as `EntityAdded(Employee)`, calculate the applicability for that
specific instance and insert them into the allowance candidates if appropriate. In that scenario the event
listen approach can be better optimized.

However building this in Arez with judicious use of {@api_url: annotations.Computed} annotated
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
