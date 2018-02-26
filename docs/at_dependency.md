---
title: @Dependency
---

The {@api_url: annotations.Dependency} annotation can be used to declare relationships between Arez
components. A component can declare that if a particular dependency is disposed then either the the component
should be disposed (if the {@api_url: action::annotations.Dependency::action()} parameter is `CASCADE`) or
the reference to the disposed component should be change to null (if the {@api_url: action::annotations.Dependency::action()}
parameter is `SET_NULL`). This feature is particularly useful if an Arez component is derived from one or
more other Arez components such as when a view model in a web application is derived from entities that
are propagated from a server.

The annotation processor will generate an internal observer if there are any `CASCADE` dependencies declared
by the component and another internal observer if there are any `SET_NULL` dependencies declared. The observers
will invoke the methods annotated with the {@api_url: annotations.Dependency} annotation within the scope of
a tracking transaction and this means the {@api_url: annotations.Dependency} annotation can be on a method
annotated with either the {@api_url: annotations.Observable} annotation or the {@api_url: annotations.Computed}
annotation. The observer will automatically track dependencies as they change.

It should be noted that only observable properties can be marked as a `SET_NULL` dependency as that is the
only mechanism that Arez can use to trigger a re-evaluation of the condition function after a reference to
a dependency has been set to null.

An example:

{@file_content: file=arez/doc/examples/at_dependency/PersonViewModel.java start_line=@ArezComponent "end_line=^}"}
