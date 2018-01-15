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
concerns. These are [@ComponentId](at_component_id.md) for defining identity and the lifecycle callback methods
`PostConstruct`, {@api_url: annotations.PreDispose} and {@api_url: annotations.PostDispose}.

## Lifecycle Callbacks

The lifecycle callbacks are used to allow the user to run custom code when the component is constructed
or disposed. The {@api_url: annotations.PreDispose} and {@api_url: annotations.PostDispose} API documentation
describes the behaviour and requirements in detail. Methods annotated with `javax.annotation.PostConstruct`
are invoked after the arez components constructor has been invoked and after all the reactive elements (i.e. the
{@api_url: Observable}, {@api_url: Observer} and {@api_url: ComputedValue} instances) have been created **but**
before any [@Autorun](at_autorun.md) methods have been scheduled for the first time.

The lifecycle callbacks are typically used to integrate with the native platform. On the web platform, the
lifecycle methods are often used to add and remove event listeners so that the Arez component can update Arez
state when an event occurs.

Below is an example that was extracted from a reactive component that exposed the "hash" part of the url as
reactive state.

{@file_content: file=arez/doc/examples/lifecycle/BrowserLocation.java start_line=@ArezComponent "end_line=^}"}
