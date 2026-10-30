---
title: @ComponentDependency
---

The {@link: arez.annotations.ComponentDependency @ComponentDependency} annotation can be used to declare relationships
between Arez components. A component can declare that if a particular dependency is disposed then either the the component
should be disposed (if the {@link: arez.annotations.ComponentDependency#action() @ComponentDependency.action}
parameter is {@link: arez.annotations.ComponentDependency.Action#CASCADE CASCADE}) or the reference to the disposed
component should be changed to null (if the {@link: arez.annotations.ComponentDependency#action() @ComponentDependency.action}
parameter is {@link: arez.annotations.ComponentDependency.Action#SET_NULL SET_NULL}). This feature is particularly
useful if an Arez component is derived from one or more other Arez components such as when a view model in a web
application is derived from entities that are propagated from a server.

If the dependency should merely stay alive while the owner stays alive, and disposal of the dependency should not
directly mutate or dispose the owner, then {@link: arez.annotations.AutoObserve @AutoObserve} is usually the better fit.

The {@link: arez.annotations.ComponentDependency @ComponentDependency} annotation can be placed on an {@link: arez.annotations.Observable @Observable}
property or on a non-observable property. The component will correctly monitor mutations of
{@link: arez.annotations.Observable @Observable} property to ensure that the correct dependency is monitored. For non-observable
properties, Arez assumes that the value returned from the method will never change and will cache the result
of invoking that method until the component is disposed.

For field targets, prefer package access. The annotation processor emits suppressable warnings for `public` fields and
for `protected` fields unless the field is inherited from a parent class in a different package.

It should be noted that only observable properties can be marked as a `SET_NULL` dependency as that is the
only mechanism that Arez can use to trigger a re-evaluation of the condition function after a reference to
a dependency has been set to null.

An example:

{@file_content: file=arez/doc/examples/at_component_dependency/PersonViewModel.java start_line=@ArezComponent "end_line=^}"}

Declared target types must provide `DisposeNotifier` at compile time. When a shared
`@ArezComponentLike` class or interface hides that capability, explicitly set
`validateTypeAtRuntime = true`; framework annotations marked with
`@ActAsArezComponent` also qualify. The actual value must implement `DisposeNotifier`.
Arbitrary `Object` and unrelated DTO declarations are rejected even with opt-in.
If the processor can already prove the capability, runtime opt-in is unnecessary
and produces the suppressible warning `Arez:UnnecessaryRuntimeTypeValidation`.

Migration: component-like targets now need explicit runtime opt-in unless their
disposal notification is known at compile time. Existing runtime-validated
`Object` or unrelated interface declarations must use an eligible component-like
type, or expose `DisposeNotifier` directly and remove the runtime flag.
