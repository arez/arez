---
title: @ArezComponent
---

Arez components are defined by the presence of an {@link: arez.annotations.ArezComponent @ArezComponent} annotation on a class or on
an interface. The annotation processor detects this annotation and scans the class and all super classes and super
interfaces implemented by the class looking for arez-specific annotations. Methods that that are annotated with an
Arez annotation and have not been overridden are processed as part of this step.

In the default production mode compilation, Arez components are just collections of [observables](observable_values.md),
[observers](observers.md) and [computable values](computable_values.md) that are defined in a single class and share
a similar lifecycle. In development mode [native components](native_components.md) are typically enabled and
components have a common name from which all elements contained in the component use as the name prefix. Components
instances also define a unique identifier that can be supplied by the developer or synthesized by the runtime.

Components can also opt into liveness-managed disposal via
{@link: arez.annotations.ArezComponent#disposeOnDeactivate() @ArezComponent.disposeOnDeactivate}. When enabled,
the component should not also be owned via {@link: arez.annotations.CascadeDispose @CascadeDispose}; mixing those
models triggers the suppressable warning `Arez:ConflictingDisposeModel`.

If another component needs to keep a `disposeOnDeactivate = true` component alive for its own lifetime, prefer
{@link: arez.annotations.AutoObserve @AutoObserve} rather than
{@link: arez.annotations.CascadeDispose @CascadeDispose}. `@AutoObserve` couples liveness without transferring
ownership of disposal.

It is recommended that you review the {@link: arez.annotations.ArezComponent @ArezComponent} API documentation for the full
description of how the annotation is used. However there are several other annotations that are component-level
concerns. These are [@ComponentId](at_component_id.md) for defining identity and the
[lifecycle callback](lifecycle_callbacks.md) methods to enable the user to inject custom code at specific points
in the components lifecycle.
