---
title: Accessing primitives
category: Other
order: 3
---

Most Arez users will make use of annotations to define the reactive components required for their application.
For most applications, most of the time this is sufficient. Very occasionally it is useful to get access to the
underlying primitives. This could mean getting access to the [Observable]({% api_url Observable %}) instance for
an `@Observable` property, getting access to the [ComputedValue]({% api_url ComputedValue %}) instance for a
`@Computed` property etc. The primary reason that these primitives need to be exposed are to enable integration
with framework specific DevTools although conceivably there are other valid use cases.

Accessing these these primitives is made possible through the user of the `@*Ref` annotations. The `@*Ref`
annotations are placed on accessor methods that return the appropriate primitive. Some of these `@*Ref` annotations
rely on either the accessors being named appropriately or on a name field aligning with the corresponding
primitives name. i.e. The `@ObservableRef` must set the `name` parameter to the same value as the associated
`@Observable` property name. Alternatively the name for `@ObservableRef` can be derived from the accessor name if
it is named according to the pattern `get[Name]Observable`. See the javadocs for the individual annotation
requirements.

The `@*Ref` annotations include the following:

* `@ObservableRef`: [API Docs]({% api_url annotations.ObservableRef %})
  This method exposes the underlying [Observable]({% api_url Observable %}) for an
  [@Observable]({% api_url annotations.Observable %}) property. 
* `@ComputedValueRef`: [API Docs]({% api_url annotations.ComputedValueRef %})
  This method exposes the underlying [ComputedValue]({% api_url ComputedValue %}) for a 
  [@Computed]({% api_url annotations.Computed %}) property. 
* `@ObserverRef`: [API Docs]({% api_url annotations.ObserverRef %})
  This method exposes the underlying [Observer]({% api_url Observer %}) for either an 
  [@Autorun]({% api_url annotations.Autorun %}) annotated method or a [@Track]({% api_url annotations.Track %})
  annotated method. 
* `@ContextRef`: [API Docs]({% api_url annotations.ContextRef %})
  This method exposes the [ArezContext]({% api_url ArezContext %}) that the component is contained within.
* `@ComponentRef`: [API Docs]({% api_url annotations.ComponentRef %})
  This method exposes the native [Component]({% api_url Component %}) associated with the component. This method
  should **NOT** be invoked unless native components are enabled. See the [Native Components]({{ site.baseurl }}/native_components)
  section of the documentation to understand how native components are enabled.

It should be noted that the `@*Ref` annotations are all marked with the `@Unsupported` annotation. The primary
reason is that it is possible that the way this feature is implemented may change in the future. Ideally use of
the `@*Ref` annotations should be restricted to framework authors to make migration in the future easier.
