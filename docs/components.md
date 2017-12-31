---
title: Components Overview
sidebar_label: Overview
---

On top of the [core conceptual model](concepts.md), Arez defines a component model driven by annotations. Annotate
a class with {@api_url: annotations.ArezComponent} to define a component, mark observable properties with the
{@api_url: annotations.Observable} annotation, mark computed properties with the {@api_url: annotations.Computed}
annotation and mark observers with either the {@api_url: annotations.Autorun} annotation or the
{@api_url: annotations.Track} annotation. The annotated classes are processed at compilation time to produce
a ready to use reactive component.
