# Task Map

- Spec: [SPEC.md](../SPEC.md)
- Status: `implementation-review`
- Current frontier: `implementation review`
- Planning reviewer: `/root/value_keys_plan_review` — passed (`1/3` attempts)
- Plan checkpoint: `automatic` — completed; user authorized implementation without a human plan checkpoint.
- Implementation reviewer: `pending` (`0/5` attempts, including confirmation)

## Full-scope validation

- Gate: `tools/check.sh`
- Evidence: `tools/check.sh` passed with exit 0 and `All Arez checks passed.` after the final code/documentation changes. All 46 aggregate targets, optimized browser tests, all 35 coverage targets/floors, packaged consumers, site and unsigned bundle checks passed. No project checks skipped.

## Tasks

| ID | Task | Status | Blocked by |
| --- | --- | --- | --- |
| T01 | [Value matching and regressions](T01-value-matching.md) | complete | None |
| T02 | [Contract and complete validation](T02-contract-validation.md) | complete | T01 |

## Review history

- Planning attempt 1/3: clean read-only reviewer `/root/value_keys_plan_review` inspected the complete tree and implementation seams. Findings: none. Coverage includes scope, authority, task ownership, cache/generator, tests, documentation and full gate. Implementation evidence remains pending.

## Promoted knowledge

- [Mapped relationships specification](../../../docs/specs/mapped-relationships.md): matching policies, stable equality, original source and first factory input.
- [Identity rationale](../../../docs/adr/0003-mapped-relationship-identity.md): identity default and explicit value matching.
- API/guide/changelog updated; domain links and specification-only audit passed.
- Closeout remains responsible for review completion, deleting this temporary tree and pushing a review PR.
