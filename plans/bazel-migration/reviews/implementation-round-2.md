# Implementation review — round 2

- Reviewer: /root/implementation_reviewer
- Limit: 5 rounds
- Result: all three exposed contract cases corrected; confirmation pending
- Reviewed implementation: d2e7e5bf3

## Findings and disposition

- P2: constructor materialization drops nonstatic enclosing generic arguments (or fails for inner types without own arguments). Preserve/recurse the enclosing parameterized type and test both forms.
- P2: unbounded wildcard and definite non-null type-variable contracts become unspecified under the preserved unmarked constructor. Emit explicit wildcard bounds and definite variable qualifiers using the original resolved mirrors/bounds.
- P2: unmarked reference overrides still disable return qualifier copying. Retain explicit return qualifiers when preserving that original scope and add a reference regression.

All three are verified exposed cases in the correction helper. Earlier root configuration and Braincheck findings are resolved. These corrections retain R04 and the accepted design; no replanning is required. All three corrections are implemented. Focused source/compiled strict goldens and the packaged consumer (including emitted-contract assertions) pass; the main full gate passed (40 default, eight production browser, 31 coverage; 2,303 JVM invocations). All 33 archives/POMs and all 350 site files match independent outputs; independent focused goldens, packaged consumer and site checks passed. Canonical spec and Unreleased notes are updated.
