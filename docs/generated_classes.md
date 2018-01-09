---
title: Generated Classes
---

Arez generates one or more classes for every Arez component. The classes generated are as follows:

* [The Enhanced Component Class](#the-enhanced-component-class)

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
