# Arez

[![Build Status](https://secure.travis-ci.org/realityforge/arez.png?branch=master)](http://travis-ci.org/realityforge/arez)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez/arez.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez%22%20a%3A%22arez%22)

Arez is designed to be a simple, efficient and scalable state management library for client-side
applications that shines no matter how complex the relationships between elements within the system.
Arez trades memory for efficiency, execution efficiency and developer efficiency. Arez avoids costly
reactions to state changes such as re-rendering views unless the underlying data actually changes and
the view needs to be re-rendered. Arez also optimizes for developer efficiency by eliminating the need
for the developer to monitor or even think about when state changes and instead react to those changes
on demand.

## Architecture v3 Notes

This sections contains the notes for the next version of Arez and just consists of a bunch of notes
that will be expanded over time as the library is fleshed out.

TODO:
* Add onDispose hook for Observers.

* Enhance the code processor so that downstream products (i.e. ArezReact) can reuse the same code used
  for generating observable state, actions etc.

* Add Async helper to extras. Can be canceled/disposed. Has an Observable state field that indicates
  current status. Triggers an "Action" on async completion. Useful for handling async http calls etc.

* Add Spy event such as ContainerDefinitionStarted/ContainerDefinitionCompleted when defining container
  instances.

* Add container (a.k.a. scope) to actions, ComputedValue and Observables???

* Enhance WhyRun and write tests for it. In browser environment use console group functions and
  colorization to make it more attractive as a debugging experience.

* Update ArezProcessor so that all errors for class are reported rather than just the first one then aborting the build.

* Start using apiInvariant to check incorrect usage of the framework. Currently we use same invariant
  checking code as verifying the behaviour of the framework. Example checks we should convert:
    - ComputedValues should not cause cycles during recalculation

* Add `@Memoized` annotation to methods that makes the method call act as a ComputedValue based on parameters.
  This could be a low priority as can "fake" it by defining a @Computed method tha invokes the method and returns
  values.

* Support optional change parameter to report*Changed that describes the change (i.e.
  AtomicChange(FromValue, ToValue), MapAdd(Key, Value), Disposed() etc). Also Propagate this change
  as a spy event. Observers opt-in to receiving these change events. If an Observer opts in then the
  accumulated changes can be pulled from the current transaction via the context. The change list will
  consist of all changes that have occurred in the order they occurred. If a Change object was not part
  of the change then an UnspecifiedChange() will be passed through. There may be some value to allowing
  it to be processed inline when it changes rather than as a reaction.

* Figure out a way how to handle "repositories" as in replicant and friends. A repository allows you to
  get all instances of entities of type. This could be handled as a series of `@Observable` maps that
  map a `@Container` annotated class + `@ContainerId` field to an instance. We could also have `@Memoized`
  queries on the repositories. To make this efficient we may need to start to support the "change parameter"
  as described above to make it efficient. (However current applications just rescan the repository each
  time they want to execute a query in many cases - which we may be able to get away with)

* Once we have Repositories it may be possible to provide a simple use debug UI - maybe somewhat inspired by
  https://github.com/zalmoxisus/mobx-remotedev

## Architecture v2 Notes

An Arez application consists of `observable` values that can change over time. `Observers` watch the
`observable` values and receive notifications when the `observable` values change. The observers implicitly
subscribe to change notifications by accessing the observable within a `tracking` transaction. (The observer
associated with a tracking transaction is called the `Tracker`.) `Observable` values can only be read within
the scope of a transaction. `Observable` values can only be modified within the scope of a `writeable`
transaction. Transactions can be nested.

Arez has the concept of a `ComputedValue` which is an `observable` value that is derived from other
`observable` values. A `ComputedValue` is both an observable and an observer. However the calculation
of the value can be passivated and not derived if there is no observers of the value. It is the mechanism
via which Arez implements the memoization optimization technique.

`Observers` can receive change notifications in a few ways;

* `ComputedValue` sends a message indicating that it is `POSSIBLY_STALE` (i.e. May have changed).
* `Observables` can send a message indicating that it is `STALE` (i.e. Has definitely changed).
* `Observables` can send a message indicating how it was changed. (i.e. Value has changed from `1` to `2`, or array index 3 was deleted).

`Observers` are typically notified at the completion of the top level transaction but may be notified
immediately on change. `ComputedValue` values are notified immediately but will not recalculate unless
accessed again or if the top-level transaction completes and they are not passivated.

There are a few separate types of observables in the system.

* `Observerable` - abstract class from which all observables descend.
* `AtomicObservable` - represents an "atomic" value where all changes involve replacing the value.
* `SetObservable` - represents an unordered set of "atomic" values.
* `ListObservable` - represents an ordered set of "atomic" values.
* `MapObservable` - a key-value collection where the key is a string and the value is an "atomic" value.

"Atomic" values include the normal data types like boolean, integer, float but also include "references".

Most of the `Observers` in the Arez system are active and will be scheduled to receive notifications when
the top level transaction is completed.

## System

The primitives described above can be used to build up more complex reactive systems. The final system we
will be building is based loosely on the concepts of [Mobx](https://mobx.js.org/). Within the system there
will be;

* _actions_: the methods responsible for changing the observable state.
* _reactions_: the methods executed if any observable state accessed within the method changes.
* _computations_: the methods that produce an observable value that will be executed if any observable
  state accessed within the method changes and any observer is observing resultant observable value.

*Actions* are methods that are wrapped in a non-tracking, writeable transaction. *Reactions* are observers
that call methods that are wrapped in a tracking transaction that may or may not be writeable. *Computations*
are passivatable observers that call methods that are wrapped in a non-writeable, tracking transaction.

# Credit

* The toolkit began life as an attempt to port [Mobx](https://mobx.js.org/) to java. Not all of the Mobx
  primitives make sense in a java world and there were places where it was felt that we could improve on
  the Mobx model in our environment so the "port" was thrown out and it became a reimplementation of similar
  concepts. However there are some places (i.e. `Transaction.completeTracking()`) where the Mobx heritage is
  clear. Credit goes to Michel Weststrate and the Mobx team for their amazing conceptual model and some
  inspirational talks.
  
* Other frameworks that influenced the development of Arez include;
  - [Meteor Tracker](https://docs.meteor.com/api/tracker.html)
  - [VueJS watchers and Computed properties](https://vuejs.org/v2/guide/computed.html)
  -  KnockoutJS [Observables](http://knockoutjs.com/documentation/observables.html) and [Computed Observables](http://knockoutjs.com/documentation/computedObservables.html) 

* The website is derived from the [Edition Template](https://github.com/CloudCannon/edition-jekyll-template)
  by [Cloud Cannon](https://cloudcannon.com/) that is licensed under the [MIT License](https://github.com/CloudCannon/edition-jekyll-template/blob/master/LICENSE).
  They also have amazing [tutorial website](https://learn.cloudcannon.com/) for developing jekyll based websites.
