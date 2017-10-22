---
title: Introduction
---

Arez is a simple, efficient and scalable state management library for client-side applications powered by a
transparent functional reactive programming core. Arez scales from simple, flat domain models to complex, highly
inter-connected graph-like domain models. Arez trades memory for execution efficiency and developer efficiency.

Anything that can be derived from application state, should be derived from application state. The derivation
should occur automatically and only when it is needed. This means that application aspects such as the user-interface,
network communication, data storage etc. should be driven by changes in application state.

The Arez architecture is based on a strict unidirectional data flow. The application state is modified by an action
and this flows through the application in a manner not unlike a spread sheet. Derivations run if any of their
dependencies change. A derivation can trigger an action but it is more likely that a user action, a network message
or some other external activity would trigger an action.

To ensure a computation only occurs once as a result of a state change, it can be stored as a "computed value". A
computed value will only be recalculated if state it is dependent upon is updated **and** another derivation depends
upon the computed value. If the recalculation produces a different value (i.e. the computed value changes) then this
change is propagated to all dependent derivations.

On top of this architecture, Arez defines a component model driven by annotations. Annotate a class with
[`@ArezComponent`](http://realityforge.org/arez/api/org/realityforge/arez/annotations/ArezComponent.html) to
define a component, mark properties as observable with the [`@Observable`](http://realityforge.org/arez/api/org/realityforge/arez/annotations/Observable.html)
annotation, mark computed properties with the [`@Computed`](http://realityforge.org/arez/api/org/realityforge/arez/annotations/Computed.html)
annotation and observers with either [`@Autorun`](http://realityforge.org/arez/api/org/realityforge/arez/annotations/Autorun.html)
or [`@Track`](http://realityforge.org/arez/api/org/realityforge/arez/annotations/Track.html). These classes
are processed at compilation time to produce a ready to use reactive component.

The goal of Arez is to avoid costly reactions to state changes such as re-rendering views unless the underlying
data actually changes and the view needs to be re-rendered. Arez also optimizes for developer joy and efficiency by
eliminating the need for the developer to monitor or even think about when state changes and instead react to those
changes on demand.
