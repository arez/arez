---
title: @Action
---

The {@api_url: annotations.Action} annotation simplifies writing [actions](actions.md). The annotation
is added to a method and that method is wrapped in a [transaction](transactions.md). The transaction can be
read-write or read-only depending on whether the {@api_url: mutation::annotations.Action::mutation()} parameter
is `true` or `false`. By default actions use read-write transactions.

The annotation also defines parameters that control the name of the action and whether the methods parameters
are logged during development mode. See the {@api_url: annotations.Action} API documentation for further details.

Actions are usually invoked in response to an event in the system, whether that been in response to a user
action or a network response.

An example:

{@file_content: file=arez/doc/examples/at_action/Wallet.java start_line=@ArezComponent "end_line=^}"}
