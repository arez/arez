---
title: Transactions
---

Transactions are a core component of an Arez application they are not directly exposed by the Arez API.
Transactions are created when [actions](actions.md) are invoked, when [computed values](computed_values.md)
are computed and when [observers](observers.md) are executed.

## Transaction Mode

All work within an Arez system occurs within the scope of a transaction. Observable data can only be
accessed within the scope of a transaction and observable data can only be updated if the transaction
is a read-write transaction.

Transactions can be nested within one another *but* a read-write transaction can only be nested within
another read-write transaction. A read-only transaction can NOT have a read-write transaction nested within
it.

This restriction means that if a method is called in a read-only transaction then the invoker can assume
that no Arez state has been mutated when the method returns. Typically read-only transactions are used by
[observers](observers.md) that want to reflect Arez state to non-Arez systems but do not want to mutate
state. The most common example being an observer that reflects Arez state to a view as in Arez applications
that use [react4j](https://react4j.github.io). The restriction makes it easy to enforce this rule.

## Tracking Transactions

Transaction can also be `tracking` or `non-tracking`. A `tracking` transaction detects accesses of
[observables](observables.md) and [computed values](computed_values.md) within the scope of a transaction.
The observables and computed values are recorded as dependencies of the running transaction. This makes it
possible for Arez to monitor the dependencies and re-scheduled the observer that created the `tracking`
transaction when the dependencies change.

## Nesting Actions

Some transactions can contain nested transactions and some transactions can not contain nested transactions.
In most cases it is easier to think about the element that initiated the transaction and what elements it can
contain.

The simplified rules are:

* An [action](actions.md) can invoke another action and access computed values but can never invoke an
  observer directly.
* A [computed value](computed_values.md) can never invoke actions or observers but can access other computed values.
* An [observer](observers.md) can access computed values and may be able to invoke actions depending on the
  configuration of the action.

It should be noted that these rules are in addition to the rule that a read-write transaction can not be nested
within a read-only transaction. Actions default to read-write transactions but may be made read-only. Observers
default to read-only transactions but may be made read-write. Computed values create read-only transactions. 

## Nesting Non-Transactional Actions

It should be noted that Arez does offer the capability to perform an action nested within a transaction
that is outside a transaction. i.e. A block of code can be run outside a transaction even if a read-only
or read-write transaction is active. This is achieved by invoking the
{@api_url: ArezContext.noTxAction(*)::ArezContext::noTxAction(arez.Procedure)} methods. This is rarely needed
and `99.5%` of code should never use this facility. This is only needed if the code invoked behaves differently
depending on whether it is nested in a transaction or not. It should be noted that this code block should not
attempt to create another transaction as this will violate the invariants of Arez and may result in
indeterminate behaviour.
