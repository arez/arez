# T02 — Contract and complete validation

- Spec coverage: R4, AC3 (documentation), AC4

## Delivers

Durable documentation and complete validation before independent implementation review and PR closeout.

## Acceptance criteria

- [x] API, guide, specification, identity rationale and changelog agree on both policies and key constraints.
- [x] Domain artifact links validate and specification-only diff passes the replacement test.
- [x] Full gate passes and implementation is ready for independent review.
- [ ] At closeout, review passes, temporary plan is removed and a PR is pushed for review.

## Validation

- Domain-modeling link validator; `git diff --check`; `tools/check.sh`.
- Independent review and closeout evidence recorded in TASKMAP before deleting this tree.

## Evidence

- Domain-modeling link validation passed: `python3 /Users/peter/.codex/skills/domain-modeling/scripts/validate_doc_links.py .`.
- Specification gates: inspected the existing contract and its last change (target validation), reused the current owner, classified matching/key stability/original-source/factory-input behavior as durable requirements, and kept map selection/testing detail in code and task evidence. Specification-only diff audit passed: every changed sentence constrains observable conformance, with no delivery sequence or implementation detail.
- Existing ADR remains the architectural owner; it now qualifies identity as the default and explains explicit equality for stable interchangeable values. Guide, API and changelog agree with it. No new glossary or deferred artifact is required.
- `git diff --check` and `tools/check.sh` passed completely (exit 0, final marker `All Arez checks passed.`). The gate includes all 46 aggregate test targets, optimized browser tests, all 35 JVM coverage targets and coverage floors, packaged JVM/J2CL consumers, hermeticity/formatting, site build/link checks, all 32 publication artifacts and unsigned bundle verification. Independent implementation review passed on attempt 1/5 with no findings; final plan removal and PR publication are the remaining closeout actions.
