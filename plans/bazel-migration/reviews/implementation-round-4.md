# Implementation review — round 4

- Reviewer: /root/implementation_reviewer
- Limit: 5 rounds
- Result: private setter finding corrected and validated; confirmation pending
- Reviewed implementation: 647513685c6039b4b1ce00a05500ddb98ea2425b

## Finding and disposition

- P2: enabling `writeOutsideTransaction` for an explicit nested nullable type-variable contract emits a private setter parameter without its qualifier. The strict generated-code checker rejects both the public-to-private call and assignment to the qualified field.
- Enabling this mode on the existing explicit parametric positive fixture reproduced both errors before correction. The separate private-header path reconstructed its type from the resolved mirror, while the public header and previously corrected local used the annotation-preserving override representation. The prior positive fixture covered only the direct setter path.
- Reuse the preserving override parameter for the private header. Both nested nullable and nonnull variants now have exact goldens in both modes; 21 strict golden invocations and seven diagnostics pass. A separate consumer of the assembled processor compiles and runs an outside-transaction setter with a nullable list element, and the existing packaged negative diagnostic still passes.

## Knowledge classification

- The accepted invariant that all generated observable setter contracts retain nested qualifiers belongs to the existing [processor nullability specification](../../../docs/specs/processor-nullability.md). It satisfies the qualification and replacement tests: every conforming generator must preserve the same declared contract, independently verifiable by compilation and runtime use. The specification-only diff adds no realization or delivery choice.
- The resolved-mirror mechanism, separate private-header path and coverage escape belong to this temporary review evidence; the representation choice belongs to code. No new architectural decision or deferred question is introduced, and no replanning is required.

The [full main gate](../review-round-4-full-gate-evidence.json) passed: 40 default targets, eight production browser targets, 31 coverage targets and 2,306 JVM invocations. [Archive inspection](../review-round-4-artifact-evidence.json) passed, and [independent checks/comparison](../review-round-4-reproducibility-evidence.json) confirms all 33 artifacts and 350 site files match. Site checks report 13,910 local links and zero broken; unsigned packaging and release stubs pass. Plan/domain links and whitespace checks pass. No strict-check suppression or policy change was added.
