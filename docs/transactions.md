---
title: Transactions
---

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

Transaction can also be `tracking` or `non-tracking`. A `tracking` transaction monitors which
[observables](observables.md) and [computed values](computed_values.md) that are accessed within the scope of a
transaction. The ultimate goal of this feature is to be able to determine the dependencies of the current
transaction so that it can be re-scheduled if these dependencies are changed.

While transactions are a core component of an Arez application but they are not directly exposed as part of the
application API. Transactions are created by [actions](actions.md) and when [observers](observers.md) are
scheduled. These concepts will be covered later in the guide.

It should be noted that Arez does offer the capability to perform an action nested within a transaction
that is outside a transaction. i.e. A block of code can be run outside a transaction even if a read-only
or read-write transaction is active. This is achieved by invoking the
{@api_url: ArezContext.noTxAction(*)::ArezContext::noTxAction(arez.Procedure)} methods. This is rarely needed
and `99.5%` of code should never use this facility. This is only needed if the code invoked behaves differently
depending on whether it is nested in a transaction or not. It should be noted that this code block should not
attempt to create another transaction as this will violate the invariants of Arez and may result in
indeterminate behaviour.
