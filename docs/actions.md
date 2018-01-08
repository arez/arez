---
title: Actions
---

Actions are the normal mechanism via which Arez state is modified. Actions wrap a function in an `untracked`
[transaction](transactions.md). Actions are typically used to make changes in an [observable](observables.md)
property that will trigger the unidrectional flow of data through an Arez application.

Arez actions can use read-only transactions and query the state of an Arez to perform some once-off side-effects
based on the state. However this is a relatively rare use-case and it is more common to use an observer to
react to changes and produce side-effects.

The {@api_url: ArezContext.action(*)::ArezContext::action(arez.Procedure,java.lang.Object...)}
methods are low-level primitives not designed to be directly used by Arez users and instead it is expected users
will use constructs like the [@Action](at_action.md) annotation from the [component](components.md) framework.

There are four main variants of the action methods provided by Arez.

* Actions that declare checked exceptions and do not return a value.
* Actions that declare checked exceptions and do return a value.
* Actions that declare no checked exceptions and do not return a value.
* Actions that declare no checked exceptions and do return a value.

Due to some peculiarities with the java type system the primitive methods that invoke actions that declare no checked
exceptions are named `safeAction` while the methods that invoke actions that declare checked exceptions are named
`action`.

An example of an action that can throw a checked exception:

{@file_content: file=arez/doc/examples/actions/ActionExample.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}

An example of an action that returns a value:

{@file_content: file=arez/doc/examples/actions/ActionExample2.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}

An example of an action that does not throw a checked exception and returns a value:

{@file_content: file=arez/doc/examples/actions/ActionExample3.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}

An example of an action that is explicitly named, uses a read-only transaction and returns a value:

{@file_content: file=arez/doc/examples/actions/ActionExample4.java "start_line=^  {" "end_line=^  }" include_start_line=false include_end_line=false strip_block=true}

While it is possible to use this low-level API it is strongly suggested that users make use of higher level
APIs such as the [@Action](at_action.md) annotation.
