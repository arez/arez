# T03 — Document, exercise browsers and validate delivery

- Status: pending
- Blocked by: T02
- Spec coverage: R10 and remaining cross-cutting portions of R1–R9; AC5 and full-scope AC1–AC4 verification

## Delivers

A documented public feature with compiled examples, real J2CL browser coverage, packaged consumer validation and a fully reviewed change ready for a PR.

## Acceptance criteria

- [ ] Promote durable obligations into a concise mapped-relationship specification; write API Javadoc and user guide with discoverability links and compiled basic/retention/explicit-callback examples.
- [ ] Explain source prefiltering, hidden-target resource behavior, retained-target source dependency configuration, guarded equality, failure guarantees and all copied memoization settings without duplicating mutable inventories.
- [ ] Add a small browser scenario covering generated mapping, identity collisions, duplicate/null sources, filtering/sorting and retention under development and optimized modes, wired into existing aggregates and packaging boundaries.
- [ ] Update Unreleased changelog; run domain links, site includes/links, formatting, complete JVM/browser and packaged consumer validation through tools/check.sh.
- [ ] Inspect the complete diff and obtain Findings: none from a fresh independent implementation reviewer within the workflow budget; fix and verify findings without broadening accepted scope.
- [ ] Leave closeout evidence and durable owners discoverable so the temporary tree can be deleted and the review PR can link issue 106 and the relevant contracts.

## Validation

- Compiled doc examples and documentation build/link checks.
- Focused browser test in default and optimized modes.
- Domain-modeling link validator against the repository root.
- tools/check.sh — strongest repository gate including JVM coverage, browser matrix and publication consumers.
- Independent implementation review — complete accepted scope and final diff.

## Evidence

- Pending.
