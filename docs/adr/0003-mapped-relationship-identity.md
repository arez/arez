# Preserve mapped target identity across result comparison

Mapped targets capture a particular source instance and may hold local state or be
disposed independently. Match sources by reference identity and guard result
equality with ordered target references before consulting a custom comparator:
ordinary `List.equals` or an unrestricted comparator can otherwise leave consumers
holding an equal but stale or disposed target. This deliberately limits custom
comparison to requesting extra notifications, unlike ordinary memoization; the
durable behavior is defined in the [mapped relationship specification](../specs/mapped-relationships.md).
