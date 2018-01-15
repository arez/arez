---
title: @ArezComponent
---

Arez components are defined by the presence of an {@api_url: annotations.ArezComponent} annotation on a class.
The annotation processor detects this annotation and scans the class and all super classes and super interfaces
implemented by the class looking for arez-specific annotations. Methods that that are annotated with an Arez
annotation and have not been overridden are processed as part of this step.

In the default production mode compilation, Arez components are just collections of [observables](observables.md),
[observers](observers.md) and [computed values](computed_values.md) that are defined in a single class and share
a similar lifecycle. In development mode [native components](native_components.md) are typically enabled and
components have a common name from which all elements contained in the component use as the name prefix. Components
instances also define a unique identifier that can be supplied by the developer or synthesized by the runtime.

It is recommended that you review the {@api_url: annotations.ArezComponent} API documentation for the full
description of how the annotation is used. However there are several other annotations that are component-level
concerns. These are [@ComponentId](at_component_id.md) for defining identity and the
[lifecycle callback](lifecycle_callbacks.md) methods to enable the user to inject custom code at specific points
in the components lifecycle.
