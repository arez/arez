---
title: Repositories
---

Components defined in Arez have an identifier by default. These identifiers may be an explicit value returned
from a method annotated with {@api_url: annotations.ComponentId} or they may be an implicit value that is
synthesized by Arez as a monotonically increasing integer for each instance of the component type.

Repositories within Arez, are a mechanism for saving references to all non-disposed instances of a particular
component type within a particular Arez [Zone](zones.md). Each components is kept in a map with the identifier as
the key. The repositories have mechanisms for creating, destroying and finding all the component instances. If
the component type has an explicit identifier marked with a {@api_url: annotations.ComponentId} annotation, the
repository also exports methods to retrieve individual components by id.

Repositories are an opt-in feature and a developer annotates an {@api_url: annotations.ArezComponent} annotated class
with the {@api_url: annotations.Repository} annotation to indicate that the annotation processor must generate a
repository. The repository generated has the structure as illustrated by the following snippet of code. Note that
this repository implementation has `findById` and `getById` which are only present because the component has a method
named `getId()` annotated with {@api_url: annotations.ComponentId}.

{@file_content: file=arez/doc/examples/repository/MyComponentRepository.java "start_line=public class"}

The repository can be interacted with in all the ways you would expect with a short snippet of usage looking
like:

{@file_content: file=arez/doc/examples/repository/RepositoryExample.java "start_line=EXAMPLE START" "end_line=EXAMPLE END" include_start_line=false include_end_line=false strip_block=true}

Repositories are Arez components and the list of instances is observable. This means that they can be integrated
as standard reactive components.

### Extensions

Sometimes you want to enhance the repository implementation to add other queries or other reactive methods
such as {@api_url: annotations.Computed} methods. The standard way to do this is to define an interface that
has a method `self()` that returns the repository type and defines one or more `default` methods that define the
extensions methods are needed. These `default` methods can be be annotated with Arez annotations (i.e.
{@api_url: annotations.Observed}, {@api_url: annotations.Computed}, {@api_url: annotations.Action} etc) and access
the underlying repository using the `self()` method.

An extension may look something like:

{@file_content: file=arez/doc/examples/repository2/MyComponentRepositoryExtension.java "start_line=public interface"}

This class must then be added to the `extensions` parameter of the {@api_url: annotations.Repository} annotation
and these methods will be available on the repository.

For example:

{@file_content: file=arez/doc/examples/repository2/MyComponent.java "start_line=@ArezComponent" "end_line={" include_end_line=false}

