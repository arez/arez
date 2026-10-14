# Implementation review — round 3

- Reviewer: /root/implementation_reviewer
- Limit: 5 rounds
- Result: parametric mixed-scope finding corrected; confirmation pending
- Reviewed implementation: 1aeb8750879433d63311207cec1cfd71377c52cc

## Finding and disposition

- P2: a nullable-bounded bare type variable nested in a marked initializer becomes unspecified in the generated unmarked constructor. Neither a forced nonnull nor unconditional nullable qualifier preserves its conditional contract.
- The case compiles before correction; a focused negative regression reproduced the missing diagnostic. Diagnose this precise unrepresentable mixed scope with guidance to mark the constructor or explicitly qualify the nested usage. Preserve marked constructors and explicit nested nullable/nonnull components with positive paired goldens.
- Positive explicit-component coverage also exposed a setter local type built from an annotation-losing resolved mirror; use the existing preserving override parameter representation for that local, rather than weakening strict checks.

This retains R04 and D05's accepted generated-initialization contract/diagnostic decision. Canonical requirements and Unreleased notes are updated. Focused strict goldens (21 invocations), seven diagnostics and the packaged positive/negative consumer pass. The full gate passed (40/8/31 targets; 2,306 JVM invocations), and all 33 artifacts/350 site files match independent outputs; targeted independent source/compiled and packaged positive/negative checks also passed; no replanning is required.
