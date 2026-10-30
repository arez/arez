# Task map

- Spec: [SPEC.md](../SPEC.md)
- Status: implementation-review
- Current frontier: implementation review
- Planning reviewer: /root/planning_review (1/3 rounds; final confirmation unused); Findings: none
- Plan checkpoint: automatic; accepted user scope and implementation/PR authority, planning review passed
- Implementation reviewer: pending (0/5 rounds; final confirmation unused)

## Full-scope validation

- Gate: tools/check.sh
- Evidence: `USE_BAZEL_VERSION=9.3.1 tools/check.sh` passed: 46 development, 10 optimized browser and 35 coverage targets; format, hermeticity, coverage floors, packaged consumers, docs/site and unsigned release verification all passed. Tool version pinned to the existing .bazelversion after an isolated Bazelisk latest-version DNS failure.

## Tasks

| ID | Task | Status | Blocked by |
| --- | --- | --- | --- |
| T01 | [Align existing annotations](T01-existing-annotations.md) | complete | None |
| T02 | [Add mapped relationship opt-in](T02-mapped-relationships.md) | complete | T01 |

## Sequencing

T01 establishes and verifies the shared validation policy before T02 applies
it to mapped target elements. The final task owns full verification and
canonical documentation promotion; PR creation follows implementation review
and removal of this temporary tree.

## Promoted knowledge

[Shared validation contract](../../../docs/specs/component-target-validation.md) and [mapped relationship contract](../../../docs/specs/mapped-relationships.md); document-link validation passed.

## Planning review

Round 1 passed: full plan and repository validation paths checked; no findings.
Inherited generic resolution and invariant-disabled J2CL behavior remain implementation test obligations.

## Knowledge classification

Shared acceptance, warning and runtime-capability obligations belong to the linked
validation specification. Existing mapped lifecycle/identity obligations remain
in the mapped specification. API usage/migration belongs to annotation Javadocs,
guides and changelog; delivery evidence and tool-version retry remain here.
No new architecture, domain vocabulary or deferred requirement was introduced.
Specification-only diff and local document links checked.
