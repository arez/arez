---
title: @Dependency
---

The {@api_url: annotations.Dependency} annotation can be used to declare relationships between Arez
components. A component can declare that if a particular dependency is disposed then either the the component
should be disposed (if the {@api_url: action::annotations.Dependency::action()} parameter is `CASCADE`) or
the reference to the disposed component should be changed to null (if the {@api_url: action::annotations.Dependency::action()}
parameter is `SET_NULL`). This feature is particularly useful if an Arez component is derived from one or
more other Arez components such as when a view model in a web application is derived from entities that
are propagated from a server.

The {@api_url: annotations.Dependency} annotation can be placed on an {@api_url: annotations.Observable}
property or on a non-observable property. The component will correctly monitor mutations of
{@api_url: annotations.Observable} property to ensure that the correct dependency is monitored. For non-observable
properties, Arez assumes that the value returned from the method will never change and will cache the result
of invoking that method until the component is disposed.

It should be noted that only observable properties can be marked as a `SET_NULL` dependency as that is the
only mechanism that Arez can use to trigger a re-evaluation of the condition function after a reference to
a dependency has been set to null.

An example:

{@file_content: file=arez/doc/examples/at_dependency/PersonViewModel.java start_line=@ArezComponent "end_line=^}"}
