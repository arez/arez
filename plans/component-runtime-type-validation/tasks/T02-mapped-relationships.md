# T02 — Add mapped relationship opt-in

- Status: pending
- Blocked by: T01
- Spec coverage: R1, R2, R3, R4, R5, R6

## Delivers

MappedRelationship supports component-like declared targets with explicit
runtime validation under the same policy. Preserve all existing mapping
contracts and demonstrate runtime liveness/failure through generated components.

## Acceptance and validation

- New annotation option defaults to false; processor matrix includes redundant
  opt-in warnings/suppression and invalid declarations.
- Strict generated compilation covers declared abstract/interface callbacks and
  inherited compiled declarations.
- Real-component tests cover hidden and retained targets, invalid factory values
  including hidden results, deactivation/disposal, and retry/cleanup behavior.
- Packaged J2CL consumers exercise component-like mappings in both build modes.
- API docs, guides, changelog and canonical shared/mapped specs match behavior.
- Run focused processor/core/integration checks, domain document link validation,
  then tools/check.sh before committing final implementation and entering review.

## Evidence

Pending.
