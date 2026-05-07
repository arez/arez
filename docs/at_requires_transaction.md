---
title: @RequiresTransaction
---

The {@link: arez.annotations.RequiresTransaction @RequiresTransaction} annotation marks a method as requiring an
existing [transaction](transactions.md). Unlike [@Action](at_action.md), the annotation does not create, wrap or
rename a transaction. Instead it fails fast in development mode if the method is invoked without the required
transaction characteristics.

Use the annotation on helper methods that are only valid when called from an existing action, observer or
computable value. The {@link: arez.annotations.RequiresTransaction#mode() @RequiresTransaction.mode} parameter can
constrain the required transaction mode and the
{@link: arez.annotations.RequiresTransaction#tracking() @RequiresTransaction.tracking} parameter can constrain
whether the existing transaction is tracking or non-tracking.

An example:

{@file_content: file=arez/doc/examples/at_requires_transaction/Wallet.java start_line=@ArezComponent "end_line=^}"}
