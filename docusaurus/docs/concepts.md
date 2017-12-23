---
title: Conceptual Overview
---

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
