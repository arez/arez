---
title: @Track
---

[Tracker observers](observers.md#tracker-observers) are defined using a pair of methods on a component; one
method is annotated with the {@api_url: annotations.Track} annotation, while the other is annotated with the
{@api_url: annotations.OnDepsChanged} annotation. Arez will call the method annotated with the
{@api_url: annotations.OnDepsChanged} annotation when it detects that the dependencies of the
{@api_url: annotations.Track} annotated method have changed.

Like [`@Autorun`](at_autorun.md) observers, tracker observers are useful for reflecting Arez state as
side-effects but tracker observers decouple the scheduling of an update from running the observer. In fact,
Arez leaves responsibility for running the observer to external code. This makes it possible for Arez to be
integrated into other frameworks that have their own scheduler.

The observer [transaction](transactions.md) is read-only by default but the
{@api_url: mutation::annotations.Autorun::mutation()} parameter can be used to set the transaction mode to
read-write. The author of the tracker observer can also omit one of the pair of annotations if the methods are
named appropriately.

The {@api_url: annotations.Track} annotation is used in libraries such as [React4j](https://react4j.github.io)
that integrate Arez into external schedulers. An example that demonstrates something similar is:

{@file_content: file=arez/doc/examples/at_track/CurrencyView.java start_line=@ArezComponent "end_line=^}"}
