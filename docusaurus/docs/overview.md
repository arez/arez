---
title: Overview
---

Arez is a simple, efficient and scalable state management library for client-side applications powered by a
transparent functional reactive programming core. Arez tracks usage of observable data and notifies observers
when the observable data changes. The library scales from simple, flat domain models to complex, highly
inter-connected graph-like domain models. Arez trades a higher memory usage for a faster execution speed
and better developer experience. Arez's optimizes for developer happiness by eliminating the need to monitor
or even think about when state changes and instead react to those changes on demand.

Anything that can be derived from application state, should be derived from application state. The derivation
should occur automatically and only when it is needed. The application elements such as the user-interface,
network communication, data storage etc. are driven by changes in application state.

### The Component Model

On top of this architecture, Arez defines a component model driven by annotations. Annotate a class with
{@api_url: annotations.ArezComponent} to define a component, mark observable properties with the
{@api_url: annotations.Observable} annotation, mark computed properties with the {@api_url: annotations.Computed}
annotation and mark observers with either the {@api_url: annotations.Autorun} annotation or the
{@api_url: annotations.Track} annotation. The annotated classes are processed at compilation time to produce
a ready to use reactive component.
