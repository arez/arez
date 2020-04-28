---
title: @ComponentId
---

The {@link: arez.annotations.ComponentId @ComponentId} annotation can be applied to a single method in the component. The
method must return a unique, non-null identifier for the component instance. This value will be converted
to a string to create the names of the underlying {@link: arez.ObservableValue}, {@link: arez.Observer} and
{@link: arez.ComputableValue} primitives.

The user typically makes use of this method to align the identifier of the Arez component with the underlying
business identifier or the identifier in the system from which it was sourced. i.e. The Arez component will
have the same id as the row in the database which the Arez component has been constructed to represent.

For example:

{@file_content: file=arez/doc/examples/component_id/Person.java start_line=@ArezComponent "end_line=^}"}

It should be noted that even if the method annotated method is not publicly accessible, the value of the
id can always be accessed using the {@link: arez.component.Identifiable#getArezId(java.lang.Object) Identifiable.getArezId(component)} method.
