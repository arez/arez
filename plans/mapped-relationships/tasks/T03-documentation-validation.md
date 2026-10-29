# T03 — Document, exercise browsers and validate delivery

- Status: complete
- Blocked by: T02
- Spec coverage: R10 and remaining cross-cutting portions of R1–R9; AC5 and full-scope AC1–AC4 verification

## Delivers

A documented public feature with compiled examples, real J2CL browser coverage, packaged consumer validation and a fully reviewed change ready for a PR.

## Acceptance criteria

- [x] Promote durable obligations into a concise mapped-relationship specification; write API Javadoc and user guide with discoverability links and compiled basic/retention/explicit-callback examples.
- [x] Explain source prefiltering, hidden-target resource behavior, retained-target source dependency configuration, guarded equality, failure guarantees and all copied memoization settings without duplicating mutable inventories.
- [x] Add a small browser scenario covering generated mapping, identity collisions, duplicate/null sources, filtering/sorting and retention under development and optimized modes, wired into existing aggregates and packaging boundaries.
- [x] Update Unreleased changelog; run domain links, site includes/links, formatting, complete JVM/browser and packaged consumer validation through tools/check.sh.
- [x] Inspect the complete diff and prepare the committed inputs for the workflow's independent implementation review; its outcome is tracked in TASKMAP.md as the post-task gate.
- [x] Leave closeout evidence and durable owners discoverable so the temporary tree can be deleted and the review PR can link issue 106 and the relevant contracts.

## Validation

- Compiled doc examples and documentation build/link checks.
- Focused browser test in default and optimized modes.
- Domain-modeling link validator against the repository root.
- tools/check.sh — strongest repository gate including JVM coverage, browser matrix and publication consumers.
- Independent implementation review — complete accepted scope and final diff.

## Evidence

- `PATH=/tmp/arez-mapped-tools.7g1LrY:$PATH tools/check.sh` passed in full: formatting, development/optimized hermeticity, product/doc compilation, assembled artifacts, public JVM/J2CL consumers, 45 test targets, all 10 optimized browser tests, 34 JVM coverage targets, documentation build/check, unsigned bundle and release-command checks.
- Coverage passed unchanged floors: core 97.89% lines/82.85% branches; processor 98.13%/90.69%. No exclusions or floor reductions.
- Mapped coverage comprises 32 real integration cases, 68 negative processor cases, 16 processor success cases (goldens in both formatting modes, source/compiled bases and variance), annotation-default parity, guarded-equality unit cases and the browser scenario in both modes. Six misplaced-annotation cases run in the existing diagnostic aggregate.
- Compiled directory, row-retention and explicit-association examples are included by the guide. Site validation checked 14,404 local file/fragment links with zero broken links. Domain-artifact link validation passed.
- Generic-target regression failed first for both source and compiled bases: ComputableValueRef compared List<Target> against original List<T>. Shared memoize validation now resolves the reference, return and activation-hook parameter types in the concrete component context. Full memoize and hook regressions passed.
- Reviewed the two generated outputs for the inherited target fixture, all new fixture declarations, and maintained formatting. The browser target consumes public packaged core/processor artifacts; the 22-target public boundary remains unchanged. Its two precise Chromium runner declarations match existing browser tests; hermeticity baseline remains empty.
- Simplify review retained the identity map/encounter list and selected-overload generation, which each protect tested behavior. No unshipped compatibility paths remain. Humanize review kept the guide focused on adoption and ownership.
- Specification-only diff audit passed: every obligation survives replacement, technical signatures constrain public behavior, and rationale/delivery evidence remain with their owners. No accepted mismatch remains.

## Durable knowledge routing

- The accepted declaration, association, identity, ordering, presentation, retention, equality, failure and lifecycle obligations pass all four specification criteria and the replacement test; no existing specification owns them. Promote them to `docs/specs/mapped-relationships.md`.
- Guarded result equality qualifies for an ADR: it constrains a public option, is surprising beside ordinary memoization, and trades comparator authority for correct instance references. Promote rationale to `docs/adr/0003-mapped-relationship-identity.md`.
- Annotation signatures/settings belong in API Javadoc; adoption and target ownership belong in the guide and compiled examples. Internal data structures, test counts and delivery sequence stay in code or this temporary tree. No new domain vocabulary, essential use case or deferred issue is needed.

## Implementation review fixes

- Round 1 P2 nested contracts: added a scoped type-use model that preserves inherited bindings and nested qualifiers while checking invariant arguments, wildcard bounds, arrays, enclosing types and comparator inputs. Java nominal assignability remains the first check; incompatible conventional methods are ignored and explicit bindings fail. Qualified arguments survive nominal supertype projection, including JDK collections, while original unmarked callback scopes remain unspecified. Removed the superseded type-argument resolver.
- Round 1 P2 generic containers: extract List/Stream arguments through inherited bindings instead of casting original type-variable returns. Validated mapped getters are always treated as collections for wrapping and cleanup. Source and compiled container fixtures pass; two runtime cases use live targets without disposeOnDeactivate, so reactivation must create a new target rather than conceal an uncleared cache by recreating a disposed value.
- New regressions cover nested factory inputs/results, both predicates, comparator input, raw contracts, ignored overloads, generic source inheritance, wildcard variance, arrays, enclosing types, intersection bounds and unmarked type variables. Variance compiles strictly in both formatting modes with an exact emitted-file set, avoiding redundant large snapshots.
- Latest complete `tools/check.sh` passed on the final fix: 45 normal test targets, 10 optimized browser targets, 34 JVM coverage targets, packaged consumers and archives, Javadoc/site and unsigned bundle. Core 97.89%/82.85%, processor 98.13%/90.69%; unchanged floors. Domain links and diff whitespace checks passed. Durable requirements are unchanged; these fixes close R4/AC2 gaps.

- Round 2 P2: permit NONNULL-to-PARAMETRIC flow after Java nominal assignability. Invariant type arguments still require both directions. Strict generic fixtures cover direct callbacks and extends/super wildcard inputs in both output modes; negative cases reject nullable-to-parametric inputs and invariant widening. Latest full `tools/check.sh` passed with 68 mapped diagnostics, 16 processor success cases and all 232 integration cases. Core coverage 97.89%/82.85%; processor 98.13%/90.69%, unchanged floors. Domain links passed; accepted durable behavior unchanged.
