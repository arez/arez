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
