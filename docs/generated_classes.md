---
title: Generated Classes
---

Arez generates one or more classes for every Arez component. The classes generated are as follows:

* [The Enhanced Component Class](#the-enhanced-component-class)
* [The Dagger Module Class](#the-dagger-module-class)
* [The Repository Component Class](#the-repository-component-class)

## The Enhanced Component Class

The enhanced component class extends the [`@ArezComponent`](at_arez_component.md) annotated class
and enhances the component with the reactive capabilities.

The class has the same name as the Arez component prefixed with `Arez_`. i.e. If the name of the
Arez component class is `com.example.MyComponent` then the enhanced component class would be named
`com.example.Arez_MyComponent`.

If the [`@ArezComponent`](at_arez_component.md) annotated class is not a top-level type, the enhanced
component class name will be include its enclosing types names, joined with an underscore. For example,
this code:

{@file_content: file=arez/doc/examples/nested/MyContainer.java start_line=public include_start_line=true}

would generate an enhanced component class named `MyContainer_Arez_MyComponent`.

The access level of the enhanced component class is package access **unless** the
[`@ArezComponent`](at_arez_component.md) annotated class is public and has at least one public constructor.
If the lifecycle of the enhanced component class is not managed by a [dependency injection](dependency_injection.md)
framework such as [Dagger2](https://google.github.io/dagger) then it is suggested that one or more static
factory methods are defined such as in the following example:

{@file_content: file=arez/doc/examples/access_level/MyComponent.java start_line=public include_start_line=true}

The enhanced component class also implements the following interfaces; {@api_url: component.Disposable},
{@api_url: component.Identifiable} and {@api_url: component.ComponentObservable}.

* The {@api_url: Disposable} interface provides a mechanism to decommission the arez component and release
  any resources associated with the component.
* The {@api_url: component.Identifiable} interface exposes the underlying identifier used by the Arez
  system to uniquely identify the component instance of this particular type.
* The {@api_url: component.ComponentObservable} allows observers to observe the component without
  observing a particular property of the component. This is typically used in conjunction with the
  {@api_url: @ArezComponent.disposeOnDeactivate::annotations.ArezComponent::disposeOnDeactivate()} parameter to ensure a component is kept
  alive while it is in use.

## The Dagger Module Class

The Dagger module class is used to enable integration with the [Dagger2](https://google.github.io/dagger)
dependency injection framework to manage Arez components. The [dependency injection](dependency_injection.md)
documentation describes the exact scenarios when this class is generated. In short this class is generated if the
annotation processor detects dagger injection is required or if the user explicitly enables dagger support.

The name of the dagger module class is the same name as the Arez component suffixed with `DaggerModule`. i.e. If
the name of the Arez component class is `com.example.MyComponent` then the dagger module class would be named
`com.example.MyComponentDaggerModule`. If the [`@ArezComponent`](at_arez_component.md) annotated class is not a
top-level type, the enhanced component class name will be include its enclosing types names, joined with an
underscore similar to the way the [enhanced component class](#the-enhanced-component-class) is named. i.e. The
inner class `com.example.MyContainer.MyComponent` would generate a dagger module class named
`com.example.MyContainer_MyComponentDaggerModule`.

The dagger module class is public, regardless of the access level of the underlying
[`@ArezComponent`](at_arez_component.md) annotated classes as public access is required to integrate with
a Dagger2 component in a different package.

It should be noted that the dagger annotation processor will also generate several other java classes during the
compilation process. These are not documented here. Consult the Dagger2 documentation for further details on the
outputs from the dagger annotation processor.

## The Repository Component Class

The repository component class is generated if the `@Repository` annotation is present as documented in the
[repositories](repositories.md) documentation. The repository class is public if the Arez component is public.
The repository component class is the same name as the Arez component suffixed with `Repository` and follows
similar conventions with respect to inner classes as the other classes.

It should be noted that the generated repository component class is an Arez component and will subsequently
generate an [enhanced component class](#the-enhanced-component-class) and potentially a
[dagger module class](#the-dagger-module-class) when dagger is enabled.
