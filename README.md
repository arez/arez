# Arez

[![Build Status](https://secure.travis-ci.org/realityforge/arez.png?branch=master)](http://travis-ci.org/realityforge/arez)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.arez/arez-core.svg?label=latest%20release"/>](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.realityforge.arez%22)

Arez is a simple, efficient and scalable state management library for client-side applications powered by a
transparent functional reactive programming core. Arez scales from simple, flat domain models to complex, highly
inter-connected graph-like domain models. Arez trades memory for execution efficiency and
developer efficiency.

Anything that can be derived from application state, should be derived from application state. The derivation
should occur automatically and only when it is needed. This means that application aspects such as the user-interface,
network communication, data storage etc. should be driven by changes in application state.

To ensure a computation only occurs once as a result of a state change, it can be stored as a computed value. A
computed value will only be recalculated if it is used by another derivation and that it depends upon changes. If
the computed value changes that change is propagated to it's dependencies.

The Arez architecture is based on a strict unidirectional data flow. The application state is modified by an action
and this flows through the application in a manner not unlike a spread sheet. Derivations run if any of their
dependencies change. A derivation can trigger an action but it is more likely that a user action, a network message
or some other external activity would trigger an action.

On top of this architecture, Arez defines a component model driven by annotations. Annotate a class with
[`@ArezComponent`](http://realityforge.org/arez/api/org/realityforge/arez/annotations/ArezComponent.html) to
define a component, mark properties as observable with the [`@Observable`](http://realityforge.org/arez/api/org/realityforge/arez/annotations/Observable.html)
annotation, mark computed property derivations with the [`@Computed`](http://realityforge.org/arez/api/org/realityforge/arez/annotations/Computed.html)
annotation and other derivations with either [`@Autorun`](http://realityforge.org/arez/api/org/realityforge/arez/annotations/Autorun.html)
or [`@Track`](http://realityforge.org/arez/api/org/realityforge/arez/annotations/Track.html). These classes
are processed at compilation time to produce a ready to use reactive component.

The goal of Arez is to avoid costly reactions to state changes such as re-rendering views unless the underlying
data actually changes and the view needs to be re-rendered. Arez also optimizes for developer joy and efficiency by
eliminating the need for the developer to monitor or even think about when state changes and instead react to those
changes on demand.

For more information about Arez, please see the [Website](http://realityforge.org/arez/).

# Credit

* [Stock Software](http://www.stocksoftware.com.au/) for providing significant support in building and maintaining
  Arez. The company was willing take a risk and base their next suite of applications on an idea and the
  bet paid off.

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
