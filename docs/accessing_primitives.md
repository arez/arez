---
title: Accessing primitives
---

Most Arez users will make use of annotations to define the reactive components required for their application.
For most applications, most of the time, this is sufficient. Very occasionally it is useful to get access to the
underlying primitives. This could mean getting access to the {@link: arez.ObservableValue} instance for
an {@link: arez.annotations.Observable @Observable} property, getting access to the {@link: arez.ComputableValue} instance for a
{@link: arez.annotations.Memoize @Memoize} property etc. It could also be accessing information about the component such
as the component name or the components type name. Access to the underlying primitives is needed for some reactive
features (i.e. explicit calls to {@link: arez.Observer#schedule()} to manually schedule
observers or {@link: arez.Observer#reportStale()}/{@link: arez.ComputableValue#reportPossiblyChanged()}
when non-arez dependencies are included in the system). However another significant reason is to enable integration
with framework specific DevTools.

Accessing these these primitives is made possible through the user of the `@*Ref` annotations. The `@*Ref`
annotations are placed on accessor methods that return the appropriate primitive. Some of these `@*Ref` annotations
rely on either the accessors being named appropriately or on a name field aligning with the corresponding
primitives name. i.e. The {@link: arez.annotations.ObservableValueRef @ObservableValueRef} must set the `name` parameter to the same value as
the associated {@link: arez.annotations.Observable @Observable} property name. Alternatively the name for the
{@link: arez.annotations.ObservableValueRef @ObservableValueRef} annotation can be derived from the accessor name if it is named according to
the pattern `get[Name]ObservableValue`. See the javadocs for the individual annotation requirements.

The `@*Ref` annotations include the following:

* {@link: arez.annotations.ComponentNameRef @ComponentNameRef}: This method exposes the underlying name of the component.
* {@link: arez.annotations.ComponentIdRef @ComponentIdRef}: This method exposes the underlying id of the component.
* {@link: arez.annotations.ComponentTypeNameRef @ComponentTypeNameRef}: This method exposes the underlying name of the component type.
* {@link: arez.annotations.ObservableValueRef @ObservableValueRef}: This method exposes the underlying {@link: arez.ObservableValue} for an {@link: arez.annotations.Observable @Observable} property.
* {@link: arez.annotations.ComputableValueRef @ComputableValueRef}: This method exposes the underlying {@link: arez.ComputableValue} for a {@link: arez.annotations.Memoize @Memoize} property.
* {@link: arez.annotations.ObserverRef @ObserverRef}: This method exposes the underlying {@link: arez.Observer} for the {@link: arez.annotations.Observe @Observe} annotated method.
* {@link: arez.annotations.ContextRef @ContextRef}: This method exposes the {@link: arez.ArezContext} that the component is contained within.
* {@link: arez.annotations.ComponentRef @ComponentRef}: This method exposes the native {@link: arez.Component}
  associated with the component. This method should **NOT** be invoked unless native components are enabled. See
  the [Native Components](native_components.md) section of the documentation to understand how native components are enabled.
