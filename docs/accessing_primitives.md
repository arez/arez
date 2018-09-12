---
title: Accessing primitives
---

Most Arez users will make use of annotations to define the reactive components required for their application.
For most applications, most of the time, this is sufficient. Very occasionally it is useful to get access to the
underlying primitives. This could mean getting access to the {@api_url: ObservableValue} instance for
an {@api_url: annotations.Observable} property, getting access to the {@api_url: ComputedValue} instance for a
{@api_url: annotations.Computed} property etc. It could also be accessing information about the component such
as the component name or the components type name. Access to the underlying primitives is needed for some reactive
features (i.e. explicit calls to {@api_url: Observer.schedule()::Observer::schedule()} to manually schedule
observers or {@api_url: Observer.reportStale()::Observer::reportStale()}/{@api_url: ComputedValue.reportPossiblyChanged()::ComputedValue::reportPossiblyChanged()}
when non-arez dependencies are included in the system). However another significant reason is to enable integration
with framework specific DevTools.

Accessing these these primitives is made possible through the user of the `@*Ref` annotations. The `@*Ref`
annotations are placed on accessor methods that return the appropriate primitive. Some of these `@*Ref` annotations
rely on either the accessors being named appropriately or on a name field aligning with the corresponding
primitives name. i.e. The {@api_url: annotations.ObservableValueRef} must set the `name` parameter to the same value as
the associated {@api_url: annotations.Observable} property name. Alternatively the name for the
{@api_url: annotations.ObservableValueRef} annotation can be derived from the accessor name if it is named according to
the pattern `get[Name]ObservableValue`. See the javadocs for the individual annotation requirements.

The `@*Ref` annotations include the following:

* {@api_url: annotations.ComponentNameRef}: This method exposes the underlying name of the component.
* {@api_url: annotations.ComponentTypeNameRef}: This method exposes the underlying name of the component type.
* {@api_url: annotations.ObservableValueRef}: This method exposes the underlying {@api_url: ObservableValue} for an {@api_url: annotations.Observable} property.
* {@api_url: annotations.ComputedValueRef}: This method exposes the underlying {@api_url: ComputedValue} for a {@api_url: annotations.Computed} property.
* {@api_url: annotations.ObserverRef}: This method exposes the underlying {@api_url: Observer} for either an {@api_url: annotations.Autorun} annotated method or a {@api_url: annotations.Track} annotated method.
* {@api_url: annotations.ContextRef}: This method exposes the {@api_url: ArezContext} that the component is contained within.
* {@api_url: annotations.ComponentRef}: This method exposes the native {@api_url: Component}
  associated with the component. This method should **NOT** be invoked unless native components are enabled. See
  the [Native Components](native_components.md) section of the documentation to understand how native components are enabled.
