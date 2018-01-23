---
title: @Autorun
---

The {@api_url: annotations.Autorun} annotation is used to define an [autorun observers](observers.md#autorun-observers).
These observers are useful for reflecting Arez state as side-effects. The observer [transaction](transactions.md) is
read-only by default but the {@api_url: mutation::annotations.Autorun::mutation()} parameter can be used to set the
transaction mode to read-write.

An example where an `@Autorun` can be used is to automatically update a view when data changes. For example,
imagine you wanted to display the value of 1 Bitcoin in Australian dollars as the value changes over time. Assuming
the currency is observable, the `@Autorun` method may look something like:

{@file_content: file=arez/doc/examples/at_autorun/CurrencyView.java start_line=@ArezComponent "end_line=^}"}

This demonstrates how easy it is to have a basic web application just using the Arez `@Autorun` primitive.
