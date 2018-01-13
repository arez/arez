---
title: Accessing primitives
---

Most Arez users will make use of annotations to define the reactive components required for their application.
For most applications, most of the time, this is sufficient. Very occasionally it is useful to get access to the
underlying primitives. This could mean getting access to the {@api_url: Observable} instance for
an {@api_url: annotations.Observable} property, getting access to the {@api_url: ComputedValue} instance for a
{@api_url: annotations.Computed} property etc. The primary reason that these primitives need to be exposed are to
enable integration with framework specific DevTools although conceivably there are other valid use cases.

Accessing these these primitives is made possible through the user of the `@*Ref` annotations. The `@*Ref`
annotations are placed on accessor methods that return the appropriate primitive. Some of these `@*Ref` annotations
rely on either the accessors being named appropriately or on a name field aligning with the corresponding
primitives name. i.e. The {@api_url: annotations.ObservableRef} must set the `name` parameter to the same value as
the associated {@api_url: annotations.Observable} property name. Alternatively the name for the
{@api_url: annotations.ObservableRef} annotation can be derived from the accessor name if it is named according to
the pattern `get[Name]Observable`. See the javadocs for the individual annotation requirements.

The `@*Ref` annotations include the following:

* {@api_url: annotations.ObservableRef}: This method exposes the underlying {@api_url: Observable} for an {@api_url: annotations.Observable} property.
* {@api_url: annotations.ComputedValueRef}: This method exposes the underlying {@api_url: ComputedValue} for a {@api_url: annotations.Computed} property.
* {@api_url: annotations.ObserverRef}: This method exposes the underlying {@api_url: Observer} for either an {@api_url: annotations.Autorun} annotated method or a {@api_url: annotations.Track} annotated method.
* {@api_url: annotations.ContextRef}: This method exposes the {@api_url: ArezContext} that the component is contained within.
* {@api_url: annotations.ComponentRef}: This method exposes the native {@api_url: Component}
  associated with the component. This method should **NOT** be invoked unless native components are enabled. See
  the [Native Components](native_components.md) section of the documentation to understand how native components are enabled.

It should be noted that the `@*Ref` annotations are all marked with the `@Unsupported` annotation. The primary
reason is that it is possible that the way this feature is implemented may change in the future. Ideally use of
the `@*Ref` annotations should be restricted to framework authors to make migration in the future easier.
