---
title: @Reference
---

A reference allows one arez component instance to refer to another arez component by id. The component author
adds the {@link: arez.annotations.Reference @Reference} annotation to an abstract method that returns a referenced object along
with a method annotated with {@link: arez.annotations.ReferenceId @ReferenceId} that provides the id of the references object.
The method annotated by {@link: arez.annotations.ReferenceId @ReferenceId} is expected to return a constant value unless it is
also annotated by {@link: arez.annotations.Observable @Observable}. The arez runtime is responsible for generating the code to
locate the referenced object by id and will return the referenced object when the object annotated with the
{@link: arez.annotations.Reference @Reference} annotation is invoked.

References are primarily used:

* when the links between components are bi-directional or can form cycles.
* when the data comes from database systems that model links using foreign keys.
* when the lookup of the referenced object should be cached but the referenced object may not be available
  when the component is constructed.

The arez runtime looks up the referenced object using a {@link: arez.Locator} instance that is registered with the
{@link: arez.ArezContext} using the {@link: arez.ArezContext#registerLocator(arez.Locator) ArezContext.registerLocator(Locator)} method. The
{@link: arez.Locator} instance can be used to lookup any arbitrary object and this makes it possible for arez objects
to lookup non-arez objects using a {@link: arez.annotations.Reference @Reference} annotated method.

The most common scenario involves the application developer creating a {@link: arez.component.TypeBasedLocator},
registering repositories in the {@link: arez.component.TypeBasedLocator} and registering the
{@link: arez.component.TypeBasedLocator} instance in the {@link: arez.ArezContext}. This would allow
{@link: arez.annotations.Reference @Reference} annotated methods to refer to any of the arez
components that are present in the registered repositories.

Consider the scenario where the application is modelling users, their memberships in groups and the permissions
associated with a group. The reference between a `Permission` and the containing `Group` could be modelled by a
reference such as:

{@file_content: file=arez/doc/examples/reference/Permission.java "start_line=@ArezComponent"}

The repository for the `Permission` looks like:

{@file_content: file=arez/doc/examples/reference/PermissionRepository.java "start_line=@ArezComponent"}

And the code to setup the locator to support this scenario:

{@file_content: file=arez/doc/examples/reference/ReferenceExample.java "start_line=EXAMPLE START" "end_line=EXAMPLE END" include_start_line=false include_end_line=false strip_block=true}

The point at which a reference will be resolved or looked up in the {@link: arez.Locator} depends upon the value
of the {@link: arez.annotations.Reference#load() @Reference.load} parameter. The default value is
{@link: arez.annotations.LinkType#EAGER} which means the reference is resolved when the arez component
is constructed or eagerly when the value of the reference id is changed.

The `load` parameter can also be set to {@link: arez.annotations.LinkType#LAZY} which means that the
reference will be resolved when the reference is accessed and cached until the value returned by the method
annotated by {@link: arez.annotations.ReferenceId @ReferenceId} changes.

The other value that the `load` parameter can be set to is {@link: arez.annotations.LinkType#EXPLICIT}.
This means that the references are resolved explicitly by the application. The application must invoke the method
{@link: arez.component.Linkable#link()} before an attempt is made to access the reference. The
`EXPLICIT` value is usually used when changes are applied in batches, across a network in non-deterministic order.

## `@Inverse`

The {@link: arez.annotations.Inverse @Inverse} annotation is used to create a link back from the target component to the
component the declared the method annotated with the {@link: arez.annotations.Reference @Reference} annotation. If a
{@link: arez.annotations.Reference @Reference} is paired with an {@link: arez.annotations.Inverse @Inverse} then it must explicitly declare
that it has an inverse either by setting the `inverse=ENABLED` parameter or one of the `inverseName` or
`inverseMultiplicity` parameters on the {@link: arez.annotations.Reference @Reference} annotation.

It should be noted that Arez deliberately requires that the inverse be annotated on both sides of the relationship
so that during incremental compiles, the annotation processor is able to inspect and validate both sides of the
relationship even if only one side has code changes. If a invalid change is made to either side then either the
annotation processor or the javac compiler will detect the problem.

The multiplicity of a relationship is defined by the type returned by the method annotated by the
{@link: arez.annotations.Inverse @Inverse} annotation and by the value of the `multiplicity` parameter on the
{@link: arez.annotations.Reference @Reference} annotation. Possible values and their implications include:

* {@link: arez.annotations.Multiplicity#MANY}: The inverse is related to many references. The type of the
  inverse must be one of `java.util.Collection`, `java.util.List` or `java.util.Set` with a type parameter
  compatible with the class containing the method annotated with the {@link: arez.annotations.Reference @Reference} annotation.
* {@link: arez.annotations.Multiplicity#ONE}: The inverse is related to exactly one reference. The type of
  the inverse must be compatible with the class containing the method annotated with the {@link: arez.annotations.Reference @Reference}
  annotation. The inverse MUST be annotated with `javax.annotation.Nonnull`
* {@link: arez.annotations.Multiplicity#ZERO_OR_ONE}: The inverse is related to one or no reference.
  The type of the inverse must be compatible with the class containing the method annotated with the
  {@link: arez.annotations.Reference @Reference} annotation. The inverse MUST be annotated with `javax.annotation.Nullable`.

So if we were to add an inverse relationship between the `Group` and `Permission` class illustrated earlier
we would need to modify both sides of the relationship. The `Permission` class would look like:

{@file_content: file=arez/doc/examples/reference2/Permission.java "start_line=@ArezComponent"}

While the `Group` class would look like

{@file_content: file=arez/doc/examples/reference2/Group.java "start_line=@ArezComponent"}
