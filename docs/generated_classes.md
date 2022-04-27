---
title: Generated Classes
---

Arez generates one or more classes for every Arez component. The classes generated are:

* [The Generated Component Subclass](#the-generated-component-subclass)

## The Generated Component Subclass

The generated component subclass extends the [`@ArezComponent`](at_arez_component.md) annotated class
and adds reactive capabilities.

The class has the same name as the Arez component prefixed with `Arez_`. i.e. If the name of the
Arez component class is `com.example.MyComponent` then the generated component subclass would be named
`com.example.Arez_MyComponent`. If the [`@ArezComponent`](at_arez_component.md) annotated class is
not a top-level type, the generated component subclass name will be include its enclosing types names,
joined with an underscore. For example, this code:

{@file_content: file=arez/doc/examples/nested/MyContainer.java start_line=public include_start_line=true}

would generate an component subclass named `MyContainer_Arez_MyComponent`.

The access level of the generated component subclass is package access. If the lifecycle of the component is
not managed by a [dependency injection](dependency_injection.md) framework then it is suggested that one or
more static factory methods are defined such as in the following example:

{@file_content: file=arez/doc/examples/access_level/MyComponent.java start_line=@ArezComponent include_start_line=true}

The generated component subclass also implements the following interfaces; {@link: arez.Disposable},
{{@link: arez.component.Identifiable} and {@link: arez.component.ComponentObservable}.

* The {@link: arez.Disposable} interface provides a mechanism to decommission the component and release
  any resources associated with the component.
* The {{@link: arez.component.Identifiable} interface exposes the underlying identifier used by the Arez
  system to uniquely identify the component instance of this particular type.
* The {@link: arez.component.ComponentObservable} allows observers to observe the component without
  observing a particular property of the component. This is typically used in conjunction with the
  {@link: arez.annotations.ArezComponent#disposeOnDeactivate() @ArezComponent.disposeOnDeactivate} parameter to ensure a component is kept
  alive while it is in use.
