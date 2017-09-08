# Arez

[![Build Status](https://secure.travis-ci.org/realityforge/arez.png?branch=master)](http://travis-ci.org/realityforge/arez)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez/arez.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez%22%20a%3A%22arez%22)

Arez is designed to be a simple, efficient and scalable state management library for client-side
applications that shines no matter how complex the relationships between elements within the system.
Arez trades memory for efficiency, execution efficiency and developer efficiency. Arez avoides costly
reactions to state changes such as re-rendering views unless the underlying data actually changes and
the view needs to be re-rendered. Arez also optimizes for developer efficiency by eliminating the need
for the developer to monitor or even think about when state changes and instead react to those changes
on demand. 

## Architecture v3 Notes

This sections contains the notes for the next version of Arez and just consists of a bunch of notes
that will be expanded over time as the library is fleshed out.

Processor Tests:
* Copy annotations on observable methods
* Copy annotations on constructor
* Copy exceptions on constructor
* Copy annotations on actions
* Copy exceptions on actions
* @Observable only required on one of getter/setter
* Override names
* How to deal with static inner classes?
* All different primitive types for fields.

TODO:
* Hooks so context-wide can get notified when observers
 - created
 - disposed
 - deactivated
 - activated
(Is all this just dev tools?)

* Introduce a separate type of invariant check that detects bad usage. Current invariant checks combine
  checks types. Separate them out so can independently control. (Usually to disable framework checks but
  enable usage checks). The checks currently are;
  - verifying that the framework internals are consistent with expectations
    - Are ComputedValue backlinked from Observables correctly
  - verifying the framework is used correctly
    - ComputedValues should not cause cycles during recalculation

* ComputedValue - where can use function that takes args (that are Observables somehow?)

Observable
* NOT YET: A list of notifications about changes in latest transaction (Or is this in
  transaction?) Notifications may collapse (i.e. atomic Observables just update with the
  last value where Array/Map will accumulate)

Observer
* Reaction (can read/write state & create side-effects). Will be scheduled after root transaction completes
  if it becomes stale during any transaction. Transactions typically explicitly indicate their transaction
  status.
* Derivation (can read state to derive state) (a.k.a. Computation in mobx). Will be re-executed if it is
  called within a transaction and is not uptodate, otherwise queued like reaction for after root
  transaction completes. Derivations are read-only transactions and should not try to change state except
  for owned or newly created observables. Derivations are implemented as extensions of Reactions but it is unclear
  whether the concept is independent of reaction as some Derivations could listen to data change messages and updated
  based on that?.
* Should we implement "set or derive" values? i.e. If set takle that value else take derived value?
* Currently the derivation can only own a single observable and that observable hitting zero listeners results
  in deactivation. It is possible a derivation can generate multiple "owned" observables?
* NOT YET: A list of notifications scheduled by dependencies if it opted in as a message receiving Observer. 


## Architecture v2 Notes

An Arez application consists of `observable` values that can change over time. `Observers` watch the
`observable` values and receive notifications when the `observable` values change. The observers can
explicitly subscribe to change notifications or can implicitly subscribe by accessing the observable
within a `tracking` transaction. (The observer associated with a tracking transaction is called the
`Tracker`.) `Observable` values can only be read within the scope of a transaction. `Observable` values
can only be modified within the scope of a `writeable` transaction. Transactions can be nested.

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
