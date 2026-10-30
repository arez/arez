# Give each contextual computation its own relationship state

A mapped factory may construct a target using implicit context, and targets can
hold local state. Give each memoization context key independent reconciliation
state so one context cannot reuse another's targets or release its retained
entries. This deliberately separates target state between contexts instead of
preserving the sharing possible through an application-owned cache helper; the
durable contract is in the [mapped relationship specification](../specs/mapped-relationships.md).
