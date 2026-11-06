# Task Map

- Spec [SPEC.md](../SPEC.md)
- Status `implementation-review`
- Current frontier empty
- Planning reviewer `/root/planning_reviewer`, passed (3/3 attempts, including replanning)
- Plan checkpoint automatic, complete after the replanning review passed
- Implementation reviewer pending (0/5 attempts, including confirmation)

## Full-scope validation

- Gate `tools/check.sh`
- Passed with default Bazel settings. See [T03 evidence](T03-browser-test-network.md#evidence).

## Tasks

| ID | Task | Status | Blocked by |
| --- | --- | --- | --- |
| T01 | [Implement the opt-in API and migration](T01-opt-in-api.md) | complete | |
| T02 | [Measure and explain compiler output](T02-verify-delivery.md) | complete | T01 |
| T03 | [Enable networking for the browser test runner](T03-browser-test-network.md) | complete | T02 |

## Promoted knowledge

- The API owns the scan contract. Migration belongs to the compiled guide and changelog. The [opt-in scans ADR](../../../docs/adr/0005-opt-in-repository-scans.md) captures the dispatch choice and the boundary. The domain link validator passed.

## Planning review history

- Attempt 1 requested live multi-repository predicate coverage and exact output comparison. The spec and T02 now require all seven operations, shared predicate dispatch, and a byte comparison in addition to size measurements.
- Attempt 2 passed with no findings. The reviewer confirmed probe coverage and the publication checkpoint.
- The user authorized a targeted browser test fix and requested verified size attribution before any question. The updated plan keeps T02 focused on compiler evidence and assigns the default full gate to T03.
- Attempt 3 passed with no findings. The reviewer confirmed the affected browser runner scope, default full gate and compiler attribution checkpoint.
