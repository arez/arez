---
title: Generated Classes
---

Arez generates one or more classes for every Arez component. The classes generated are as follows:

* [The Enhanced Component Class](#the-enhanced-component-class)
* [The Dagger Module Class](#the-dagger-module-class)

## The Enhanced Component Class

The enhanced component class extends the [`@ArezComponent`](at_arez_component.md) annotated class
and enhances the component with the reactive capabilities.

The class has the same name as the Arez component prefixed with `Arez_`. i.e. If the name of the
Arez component class is `com.example.MyComponent` then the enhanced component class would be named
`com.example.Arez_MyComponent`.

If the [`@ArezComponent`](at_arez_component.md) annotated class is not a top-level type, the enhanced
component class name will be include its enclosing types’ names, joined with an underscore. For example,
this code:

{@file_content: file=arez/doc/examples/nested/MyContainer.java start_line=public include_start_line=true}

would generate an enhanced component class named `MyContainer_Arez_MyComponent`.

The access level of the enhanced component class is package access **unless** the
[`@ArezComponent`](at_arez_component.md) annotated class is public and has at least one public constructor.
If the lifecycle of the enhanced component class is not managed by a [dependency injection](dependency_injection.md)
framework such as [Dagger2](https://google.github.io/dagger) then it is suggested that one or more static
factory methods such as in the following example:

{@file_content: file=arez/doc/examples/access_level/MyComponent.java start_line=public include_start_line=true}

## The Dagger Module Class

The Dagger module class is used to enable integration with the [Dagger2](https://google.github.io/dagger)
dependency injection framework to manage Arez components. The [dependency injection](dependency_injection.md)
documentation describes the exact scenarios when this class is generated. In short this class is generated if the
annotation processor detect dagger injection is required or if the user explicitly enables dagger support.

The dagger module class is public, regardless of the access level of the underlying
[`@ArezComponent`](at_arez_component.md) annotated classes access modifier as it is required to integrate with
a Dagger2 component in a different package.

It should be noted that the dagger annotation processor will also generate several other java classes during the
compilation process. These are not documented here. Consult the Dagger2 documentation for further details on the
outputs from the dagger annotation processor.
