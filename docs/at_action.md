---
title: @Action
---

The {@link: arez.annotations.Action @Action} annotation simplifies writing [actions](actions.md). The annotation
is added to a method and that method is wrapped in a [transaction](transactions.md). The transaction can be
read-write or read-only depending on whether the {@link: arez.annotations.Action#mutation() @Action.mutation} parameter
is `true` or `false`. By default actions use read-write transactions.

The annotation also defines parameters that control the name of the action and whether the methods parameters
are logged during development mode. See the {@link: arez.annotations.Action @Action} API documentation for further details.
Use {@link: arez.annotations.Action#skipIfDisposed @Action.skipIfDisposed} to skip an action when the owning component
is disposing or disposed; the action must be void and the skip emits a spy event.

Actions are usually invoked in response to an event in the system, whether that been in response to a user
action or a network response.

An example:

{@file_content: file=arez/doc/examples/at_action/Wallet.java start_line=@ArezComponent "end_line=^}"}
