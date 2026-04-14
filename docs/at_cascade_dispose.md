---
title: @CascadeDispose
---

The {@link: arez.annotations.CascadeDispose @CascadeDispose} annotation is used to mark fields that contain values that must
be disposed when the component is disposed. This is a short-hand way of cleaning up arez components and other
resources that have been created or are owned by the component when the component is disposed.

If the referenced value already uses liveness-managed disposal via
{@link: arez.annotations.ArezComponent#disposeOnDeactivate() disposeOnDeactivate = true} and should only be kept alive
while the owner is alive, prefer {@link: arez.annotations.AutoObserve @AutoObserve}. `@CascadeDispose` is for
ownership; `@AutoObserve` is for liveness.

If the target is an {@link: arez.annotations.ArezComponent @ArezComponent} configured with
{@link: arez.annotations.ArezComponent#disposeOnDeactivate() disposeOnDeactivate = true} then the annotation processor
will emit the suppressable warning `Arez:ConflictingDisposeModel`. Disposal should be managed either by liveness
or explicitly, but not both.

if the {@link: arez.annotations.CascadeDispose @CascadeDispose} annotation did not exist, the component author would be responsible
for cleaning up the resource manually in a method annotated with {@link: arez.annotations.PreDispose @PreDispose}.

An example using manual mechanisms for disposing a sub-resource of a component:

{@file_content: file=arez/doc/examples/cascade_dispose/MyService.java "start_line=@ArezComponent"}

Sometimes when the component author added a new sub-component, the component author would forget to add the
required dispose call in the {@link: arez.annotations.PreDispose @PreDispose} annotated method. This resulted in a resource
leak an inevitably some amount of time was spent detecting and fixing the problem.

Alternatively the author can annotate the field with the {@link: arez.annotations.CascadeDispose @CascadeDispose} annotation
and the annotation processor generates the code required for disposing the sub-component. In practice this
resulted in less time chasing down resource leaks and slightly less code to maintain. The only disadvantage
was that the fields have to be made non-private so that the generated sub-class can access the field.

For field visibility, prefer package access. The annotation processor emits suppressable warnings for `public` fields
and for `protected` fields unless the field is inherited from a parent class in a different package.

For example:

{@file_content: file=arez/doc/examples/cascade_dispose2/MyService.java "start_line=@ArezComponent"}
