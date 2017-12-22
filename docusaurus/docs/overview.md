---
title: Overview
---

Arez is a simple, efficient and scalable state management library for client-side applications powered by a
transparent functional reactive programming core. Arez tracks usage of observable data and notifies observers
when the observable data changes. The library scales from simple, flat domain models to complex, highly
inter-connected graph-like domain models. Arez trades a higher memory usage for a faster execution speed.
Arez's goal is to do this while optimizing for developer happiness by eliminating the need for the developer
to monitor or even think about when state changes and instead react to those changes on demand.

Anything that can be derived from application state, should be derived from application state. The derivation
should occur automatically and only when it is needed. The application elements such as the user-interface,
network communication, data storage etc. are driven by changes in application state.

### The Architecture

The Arez architecture is based on a strict unidirectional data flow:

* _Actions_ modify _observable properties_.
* _Observable properties_ notify _observers_ when they change.
* _Computed values_ are values derived from _observable properties_ and other _computed values_. _Computed values_
  notify _observers_ when they change.
* _Observers_ update the user-interface, network communication, etc to reflect the current application state.

Actions _may_ be triggered by _observers_ when they update from application state but it is more likely that a
user action, a network message or some other external activity would trigger an action.

<div class="svg-figure">

{@include: SimpleDataflowNodes.svg}

</div>

A computed value will only be recalculated if it is "active". A computed value is active if it is a dependency
of an observer or a dependency of another active computed value. This results in an architecture where changes
flow through the application in a manner not unlike in a spreadsheet and observers react to the changes when
their dependencies change. Data flows in a directed graph from actions to observers and the observers react
to the changes.

<div class="svg-figure">

{@include: DataflowNodes.svg}

</div>

### The Component Model

On top of this architecture, Arez defines a component model driven by annotations. Annotate a class with
{@api_url: annotations.ArezComponent} to define a component, mark observable properties with the
{@api_url: annotations.Observable} annotation, mark computed properties with the {@api_url: annotations.Computed}
annotation and mark observers with either the {@api_url: annotations.Autorun} annotation or the
{@api_url: annotations.Track} annotation. The annotated classes are processed at compilation time to produce
a ready to use reactive component.
