# Arez

[![Build Status](https://secure.travis-ci.org/realityforge/arez.png?branch=master)](http://travis-ci.org/realityforge/arez)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez/arez-core.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez%22)

Arez is designed to be a simple, efficient and scalable state management library for client-side
applications that shines no matter how complex the relationships between elements within the system.
Arez trades memory for efficiency, execution efficiency and developer efficiency. Arez avoids costly
reactions to state changes such as re-rendering views unless the underlying data actually changes and
the view needs to be re-rendered. Arez also optimizes for developer efficiency by eliminating the need
for the developer to monitor or even think about when state changes and instead react to those changes
on demand.

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

Most of the `Observers` in the Arez system are active and will be scheduled to receive notifications when
the top level transaction is completed.

* _actions_: the methods responsible for changing the observable state.
* _reactions_: the methods executed if any observable state accessed within the method changes.
* _computations_: the methods that produce an observable value that will be executed if any observable
  state accessed within the method changes and any observer is observing resultant observable value.

*Actions* are methods that are wrapped in a non-tracking, writeable transaction. *Reactions* are observers
that call methods that are wrapped in a tracking transaction that may or may not be writeable. *Computations*
are passivatable observers that call methods that are wrapped in a non-writeable, tracking transaction.

# Credit

* The toolkit began life as an attempt to port [Mobx](https://mobx.js.org/) to java. Not all of the Mobx
  primitives made sense in a java world. There were also places where it was felt that we could improve on
  the Mobx model, at least for our environment. The idea of a "port" was soon discarded and Arez became a
  reimplementation of similar concepts. However there are some places (i.e. `Transaction.completeTracking()`)
  where the Mobx heritage is clear. Credit goes to Michel Weststrate and the Mobx team for their clean conceptual
  model and some inspirational talks.

* Other frameworks that influenced the development of Arez include;
  - [Meteor Tracker](https://docs.meteor.com/api/tracker.html)
  - [VueJS watchers and Computed properties](https://vuejs.org/v2/guide/computed.html)
  -  KnockoutJS [Observables](http://knockoutjs.com/documentation/observables.html) and [Computed Observables](http://knockoutjs.com/documentation/computedObservables.html)

* The website is derived from the [Edition Template](https://github.com/CloudCannon/edition-jekyll-template)
  by [Cloud Cannon](https://cloudcannon.com/) that is licensed under the [MIT License](https://github.com/CloudCannon/edition-jekyll-template/blob/master/LICENSE).
  They also have amazing [tutorial website](https://learn.cloudcannon.com/) for developing jekyll based websites.
