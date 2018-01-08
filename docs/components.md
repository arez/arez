---
title: Components Overview
sidebar_label: Overview
---

On top of the [core conceptual model](concepts.md), Arez defines a component model driven by annotations.

* Annotate a class with [@ArezComponent](at_arez_component.md) to define a component.
* Mark observable properties with the [@Observable](at_observable.md) annotation.
* Mark computed properties with the [@Computed](at_computed.md) annotation.
* Mark observers with either the [@Autorun](at_autorun.md) annotation or the [@Track](at_track.md) annotation.
* Annotate code that makes changes to observable data with the [@Action](at_action.md) annotation.

The annotated classes are processed at compilation time to produce a ready to use reactive component. The
[generated classes](generated_classes.md) are enhanced with required infrastructure required to integrate
with the Arez core framework.
