# Task map

- Spec: [SPEC.md](../SPEC.md)
- Status: implementing
- Current frontier: T02
- Planning reviewer: /root/planning_review (1/3 rounds; final confirmation unused); Findings: none
- Plan checkpoint: automatic; accepted user scope and implementation/PR authority, planning review passed
- Implementation reviewer: pending (0/5 rounds; final confirmation unused)

## Full-scope validation

- Gate: tools/check.sh
- Evidence: pending

## Tasks

| ID | Task | Status | Blocked by |
| --- | --- | --- | --- |
| T01 | [Align existing annotations](T01-existing-annotations.md) | complete | None |
| T02 | [Add mapped relationship opt-in](T02-mapped-relationships.md) | pending | T01 |

## Sequencing

T01 establishes and verifies the shared validation policy before T02 applies
it to mapped target elements. The final task owns full verification and
canonical documentation promotion; PR creation follows implementation review
and removal of this temporary tree.

## Promoted knowledge

Pending: shared component target validation specification and mapped specification link.

## Planning review

Round 1 passed: full plan and repository validation paths checked; no findings.
Inherited generic resolution and invariant-disabled J2CL behavior remain implementation test obligations.
