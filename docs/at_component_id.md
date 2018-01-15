---
title: @ComponentId
---

The {@api_url: annotations.ComponentId} annotation can be applied to a single method in the component. The
method must return a unique, non-null identifier for the component instance. This value will be converted
to a string to create the names of the underlying {@api_url: Observable}, {@api_url: Observer} and
{@api_url: ComputedValue} primitives. If the [`@Repository`](repositories.md) annotation is present on the
component, the method will also return the key that is used to store the component in a map.

The user typically makes use of this method to align the identifier of the Arez component with the underlying
business identifier or the identifier in the system from which it was sourced. i.e. The Arez component will
have the same id as the row in the database which the Arez component has been constructed to represent.

For example:

{@file_content: file=arez/doc/examples/component_id/Person.java start_line=@ArezComponent "end_line=^}"}
