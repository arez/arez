---
title: Components Overview
sidebar_label: Overview
---

On top of the [core conceptual model](concepts.md), Arez defines a component model driven by annotations. Annotate
a class with {@api_url: annotations.ArezComponent} to define a component, mark observable properties with the
[@Observable](at_observable.md) annotation, mark computed properties with the [@Computed](at_computed.md)
annotation and mark observers with either the [@Autorun](at_autorun.md) annotation or the
[@Track](at_track.md) annotation. Annotate code that makes changes to observable data with the [@Action](at_action.md)
annotation. The annotated classes are processed at compilation time to produce a ready to use reactive component.
