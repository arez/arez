# Implementation review — round 1

- Reviewer: /root/implementation_reviewer
- Limit: 5 rounds
- Result: three actionable findings, all three findings corrected; confirmation pending
- Scope: full committed implementation through 2962719eb55b798b2d290acea3758fc2bbb41b7d

## Findings and disposition

- P1: downstream module setup cannot resolve because transitive root overrides are ignored and patches must belong to the consumer root. Provide complete copied root configuration and a real downstream Bazel regression.
- P2: public core hides Braincheck, which generated code directly references. Export that mandatory dependency and verify strict downstream compilation.
- P2: generated overrides and constructors lose original unmarked scopes. Preserve declaration scopes and explicitly retain marked synthetic initializer contracts; add source/compiled and nested-position goldens.

All findings are verified and accepted. These corrections preserve R04/R08/R16 and the committed significant decisions; no replanning is required. Focused JSpecify targets and the separate strict public Bazel consumer pass. Canonical requirements/ADR and consumer instructions are updated; plan and domain links pass. Both full check gates passed (40 default, eight production browser, 31 coverage targets; 2,303 JVM invocations). All 33 artifact hashes and all 350 site files match independent outputs. Evidence is linked from the task map. Confirmation by the same reviewer is next.
