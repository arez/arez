# Component runtime type validation

## Source and authority

The user accepted the shared validation matrix for AutoObserve,
ComponentDependency and MappedRelationship, then authorized implementation and
a PR in this chat. The original mapped-relationship prompt supplies lifecycle,
callback, nullness, inheritance, filtering and packaged J2CL constraints. The
user explicitly preferred AGENTS.md over duplicating repository process rules.
Grill-me was requested only if additional context was needed; the accepted
matrix resolves the design without an interview. There is no human plan checkpoint.

## Problem and outcome

Shared component-like classes and interfaces hide generated capabilities.
Mapped relationships cannot currently opt into runtime validation, and the two
existing runtime options have inconsistent acceptance rules. Make all three
annotations use the same explicit, bounded escape hatch with the appropriate
capability: ComponentObservable or DisposeNotifier.

## Requirements and acceptance

- R1: Types whose required capability is proven at compilation remain accepted.
  Explicit runtime validation on these types produces a suppressible unnecessary
  configuration warning, independent of component-like marking.
- R2: Otherwise, a declared type must have existing component-like eligibility
  (@ArezComponentLike or an annotation marked with @ActAsArezComponent) and
  validateTypeAtRuntime=true. Missing opt-in and arbitrary Object/DTO declarations
  fail compilation. Apply to fields and methods where each annotation supports them.
- R3: MappedRelationship gains validateTypeAtRuntime=false. Preserve its existing
  strict default, callback resolution against declared types, inherited/compiled
  contracts, nullness and generated cache lifecycle.
- R4: Runtime capability failures use existing casts, observation and invariant
  mechanisms. Hidden and retained mapped targets remain observed. Preserve
  invariant-enabled and disabled behavior without requiring identical cross-mode
  diagnostics. Do not add runtime branches or skip unsupported targets.
- R5: Existing mapped identity, filtering, retention, disposal, cleanup, failure
  recovery, source equality and context-parameter behavior remain intact.
- R6: Document the shared contract and breaking ComponentDependency migration.
  Tests prove the matrix, suppression, real generated-component lifecycle,
  invalid hidden targets, inheritance/compiled declarations, and packaged J2CL
  consumption. Complete repository verification and create a PR.

Each requirement is its acceptance criterion: processor cases must compile,
warn or reject as stated; runtime assertions must observe lifecycle or failure
through real generated components; the repository gate must pass.

## Significant decisions

| Decision | Rationale and impact | User verification |
| --- | --- | --- |
| Accept and warn for redundant opt-in | Matches existing unnecessary-setting diagnostics; can fail builds that treat warnings as errors | Warning is suppressible |
| Require marker and explicit opt-in for uncertain targets | Makes the escape hatch intentional and excludes unrelated objects | ComponentDependency callers may need marking and opt-in |
| Use the same policy for all three annotations | User explicitly accepted project-wide consistency | Migration described in docs and changelog |
| Reuse runtime mechanisms | Cache already observes before filtering and for retention | No cache policy change |

## Technical and testing decisions

Keep validation within ArezProcessor and reuse existing capability predicates.
Use one shared diagnostic policy helper where it removes the current duplicate
branches. Preserve the generation path and descriptor's declared target types.
Use processor matrix tests for both field and method paths, strict generated
compilation and inherited compiled cases. Migrate affected existing fixtures
instead of retaining tests for obsolete permissive behavior. Exercise opt-in
mapped relationships with real interface and abstract-class targets, including
hidden/retained liveness and invalid factory results. Extend the existing packaged
consumer examples rather than introducing another harness. Use the full gate
from AGENTS.md before implementation review.

The new shared durable contract belongs in docs/specs/component-target-validation.md;
update the mapped-relationship specification to link it. API docs and guides own
usage/migration instructions. No architectural choice requires a new ADR.

## Boundaries

No Rose changes, new target equality, context parameters, compatibility shims,
or unrelated framework cleanup. Preserve all existing later enhancements and
local work. No release or merge is authorized; PR creation is authorized.

## Open questions

None.
