---
title: @Reference
---

A reference allows one arez component instance to refer to another arez component by id. The component author
adds the {@api_url: annotations.Reference} annotation to an abstract method that returns a referenced object along
with a method annotated with {@api_url: annotations.ReferenceId} that provides the id of the references object.
The method annotated by {@api_url: annotations.ReferenceId} is expected to return a constant value unless it is
also annotated by {@api_url: annotations.Observable}. The arez runtime is responsible for generating the code to
locate the referenced object by id and will return the referenced object when the object annotated with the
{@api_url: annotations.Reference} annotation is invoked.

References are primarily used:

* when the links between components are bi-directional or can form cycles.
* when the data comes from database systems that model links using foreign keys. 
* when the lookup of the referenced object should be cached but the referenced object may not be available
  when the component is constructed.

The arez runtime looks up the referenced object using a {@api_url: Locator} instance that is registered with the
{@api_url: ArezContext} using the {@api_url: ArezContext::registerLocator(arez.Locator)} method. The
{@api_url: Locator} instance can be used to lookup any arbitrary object and this makes it possible for arez objects
to lookup non-arez objects using a {@api_url: annotations.Reference} annotated method.

The most common scenario involves the application application developer creating a
{@api_url: component.TypeBasedLocator}, registering repositories created by the arez runtime when the
[`@Repository`](repositories.md) annotation is present and then registering the {@api_url: component.TypeBasedLocator}
instance in the {@api_url: ArezContext}. This would allow `@Reference` annotated methods to refer to any of the arez
components that are present in the registered repositories.

Consider the scenario where the application is modelling users, their memberships in groups and the permissions
associated with a group. The reference between a `Permission` and the containing `Group` could be modelled by a
reference such as:

{@file_content: file=arez/doc/examples/reference/Permission.java "start_line=@ArezComponent"}

And the code to setup the locator to support this scenario:

{@file_content: file=arez/doc/examples/reference/ReferenceExample.java "start_line=EXAMPLE START" "end_line=EXAMPLE END" include_start_line=false include_end_line=false strip_block=true}

The point at which a reference will be resolved or looked up in the {@api_url: Locator} depends upon the value
of the {@api_url: Reference.load::annotations.Reference::load()} parameter. The default value is
{@api_url: EAGER::annotations.LinkType::EAGER} which means the reference is resolved when the arez component
is constructed or eagerly when the value of the reference id is changed.

The `load` parameter can also be set to {@api_url: LAZY::annotations.LinkType::LAZY} which means that the
reference will be resolved when the reference is accessed and cached until the value returned by the method
annotated by {@api_url: annotations.ReferenceId} changes.

The other value that the `load` parameter can be set to is {@api_url: EXPLICIT::annotations.LinkType::EXPLICIT}.
This means that the references are resolved explicitly by the application. The application must invoke the method
{@api_url: Linkable.link()::component.Linkable::link()} before an attempt is made to access the reference. The
`EXPLICIT` value is usually used when changes are applied in batches, across a network in non-deterministic order. 
