# Task Map

- Spec [SPEC.md](../SPEC.md)
- Status `implementing`
- Current frontier `T02`
- Planning reviewer `/root/planning_reviewer`, passed (2/3 attempts, including confirmation)
- Plan checkpoint automatic, complete after independent review passed on attempt 2
- Implementation reviewer pending (0/5 attempts, including confirmation)

## Full-scope validation

- Gate `tools/check.sh`
- Evidence pending

## Tasks

| ID | Task | Status | Blocked by |
| --- | --- | --- | --- |
| T01 | [Implement the opt-in API and migration](T01-opt-in-api.md) | complete | |
| T02 | [Verify compiler output and close delivery](T02-verify-delivery.md) | pending | T01 |

## Promoted knowledge

- The API owns the scan contract. Migration belongs to the compiled guide and changelog. The [opt-in scans ADR](../../../docs/adr/0005-opt-in-repository-scans.md) captures the dispatch choice and the boundary. The domain link validator passed.

## Planning review history

- Attempt 1 requested live multi-repository predicate coverage and exact output comparison. The spec and T02 now require all seven operations, shared predicate dispatch, and a byte comparison in addition to size measurements.
- Attempt 2 passed with no findings. The reviewer confirmed probe coverage and the publication checkpoint.
