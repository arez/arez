---
title: @AutoObserve
---

The {@link: arez.annotations.AutoObserve @AutoObserve} annotation keeps a referenced
{@link: arez.component.ComponentObservable ComponentObservable} observed for the full alive lifetime of the owning
component. This is primarily useful when a component references another
{@link: arez.annotations.ArezComponent @ArezComponent} configured with
{@link: arez.annotations.ArezComponent#disposeOnDeactivate() disposeOnDeactivate = true} and the owner should keep
that referenced component alive while the owner itself is alive.

The annotation can be placed on:

* a final field
* a concrete zero-argument method
* an abstract {@link: arez.annotations.Observable @Observable} getter
* an abstract {@link: arez.annotations.Reference @Reference} accessor

For field targets, prefer package access. The annotation processor emits suppressable warnings for `public` fields and
for `protected` fields unless the field is inherited from a parent class in a different package.

When the target can return null, the generated auto-observer skips null values safely. When paired with
{@link: arez.annotations.Reference @Reference}, auto-observation forces the reference to resolve, even for
{@link: arez.annotations.LinkType#LAZY LAZY} references.

Use {@link: arez.annotations.AutoObserve @AutoObserve} when you want liveness coupling. Use
{@link: arez.annotations.CascadeDispose @CascadeDispose} when the owner should explicitly dispose the target. Use
{@link: arez.annotations.ComponentDependency @ComponentDependency} when disposal of the target should trigger disposal
or nulling of the owner reference.

An example:

{@file_content: file=arez/doc/examples/at_auto_observe/Dashboard.java start_line=@ArezComponent "end_line=^}"}
