# Multi OnDepsChange Requirements

## Mission

- Allow a single logical `@Observe`/observer to have multiple matched `@OnDepsChange` hooks while preserving current single-hook behavior and generated-code conventions.

## Scope Boundaries

- In scope: processor parsing/linking, observe descriptor storage, observe code generation, processor fixtures/tests, `CHANGELOG.md`, and structured-delivery artifacts.
- Out of scope: runtime `arez.Observer` API changes, repeatable annotations on a single method, changes to non-observe hook types, and unrelated processor refactors.

## Locked Decisions

- Multiple matched `@OnDepsChange` methods targeting the same observe name are legal.
- All matched hooks run in deterministic processor encounter order.
- Explicit `@OnDepsChange` methods and convention-derived `onXDepsChange(...)` methods participate equally.
- Mixed hook signatures are supported: parameterless hooks and hooks accepting `arez.Observer` may coexist.
- Single-hook code generation remains unchanged.
- Multi-hook code generation synthesizes one private framework dispatcher method per observe.
- If one hook throws, later hooks do not run.
- For `executor = INTERNAL`, a multi-hook observe is schedulable if there is an `@ObserverRef` or at least one `@OnDepsChange(Observer)` hook.
- Inheritance is additive when multiple hook methods are present in the discovered method set.
- Unresolved observe linkage should produce one diagnostic for the logical observe, anchored to the first matched hook in encounter order.
- Update `CHANGELOG.md` because this is user-visible processor behavior.

## Command Surface

- `ObserveDescriptor` should track an ordered collection of on-deps-change methods plus helper queries for none/single/multiple-hook cases.
- `ArezProcessor` should link all matched hooks for a logical observe instead of rejecting duplicates.
- `ComponentGenerator` should:
  - keep existing single-hook lambda/adapter behavior intact,
  - synthesize a dispatcher only for the multi-hook case,
  - route mixed hook signatures through that dispatcher.

## Quality Gates

- Targeted iteration gate:
  - `bundle exec buildr processor:test`
- Required full gate:
  - `bundle exec buildr processor:test`

## Open Questions Register

| id | status | question | context | options | tradeoffs | recommended_default | user_decision | artifacts_updated |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Q-01 | resolved | Should multiple matched `@OnDepsChange` hooks all run? | The old model rejected duplicate hooks for a logical observe. | run all; keep rejecting duplicates; pick one winner | Running all preserves user intent and avoids hidden precedence rules. | run all | run all | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-02 | resolved | What execution order should multiple hooks use? | Order must stay deterministic across class/interface scans. | encounter order; sort by name; prefer explicit hooks | Encounter order matches existing processor behavior and avoids synthetic precedence. | encounter order | preserve declaration/discovery order | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-03 | resolved | Should mixed signatures share one synthesized dispatcher? | Some hooks may take `Observer`, others may be parameterless. | single dispatcher; separate runtime callbacks | One dispatcher keeps generation/runtime wiring simple and uniform. | single dispatcher | single dispatcher | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-04 | resolved | Should later hooks run if an earlier hook throws? | Multi-hook dispatch could either continue or preserve current fail-fast behavior. | fail fast; continue after errors | Fail-fast preserves single-hook semantics and existing observer error handling. | fail fast | do not run later hooks after a throw | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-05 | resolved | Should single-hook codegen remain unchanged? | The feature only requires new synthesis for multi-hook observes. | keep single-hook output; normalize all hooks through synthesis | Keeping single-hook output minimizes fixture churn and regression risk. | keep single-hook output | keep single-hook output unchanged | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-06 | resolved | Are mixed explicit and convention-derived hooks legal for one observe? | Matching can come from annotations or naming conventions. | allow all matched hooks; explicit-only; explicit beats implicit | Treating all matched hooks equally keeps the feature consistent with existing conventions. | allow all matched hooks | allow all matched hooks | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-07 | resolved | What makes `executor = INTERNAL` schedulable for multi-hook observes? | Current logic requires either `@ObserverRef` or an `Observer` parameter on the lone hook. | any `Observer`-accepting hook or ref; require ref always; require every hook to accept `Observer` | Any observer source is sufficient for the generated dispatcher. | any `Observer`-accepting hook or ref | any `Observer`-accepting hook or ref | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-08 | resolved | Should convention-derived unannotated hooks participate in the list? | Auto-linked hooks already exist today. | include them; restrict to annotated hooks | Restricting them would create inconsistent behavior based on annotation spelling only. | include them | include them | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-09 | resolved | How should unresolved multi-hook linkage be diagnosed? | Multiple hooks may point at an observe that does not exist. | single logical error; one error per hook | One diagnostic is clearer because the fault is the missing observe, not hook count. | single logical error | single logical error | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
| Q-10 | resolved | How should inherited hooks behave when multiple are present? | A subtype may add a second hook for an observe already hooked in a base type/interface. | additive; subtype replaces base | Additive behavior matches the “all matched hooks run” rule unless normal Java method overriding removes a hook from discovery. | additive | additive when both methods are discovered | `00-requirements.md`, `10-implementation-plan.md`, `20-task-board.yaml` |
