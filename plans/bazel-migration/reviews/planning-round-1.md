# Planning review — round 1

- Reviewer: /root/planning_reviewer
- Limit: 3 rounds
- Result: two blocking findings
- Disposition: corrected; confirmation requested from the same reviewer in round 2

## Findings

1. Browser example sources lacked an owned JSpecify/strict Java compilation gate.
   T13-T15 covered JVM consumers while T20-T22 only promised J2CL/browser execution.
   Existing promise/location examples still import javax.annotation.Nonnull.
2. Artifact bytecode checks did not distinguish Java 17 owned/generated classes
   from Java 21 dependencies shaded into processor jars. Pinned Proton targets
   Java 21; the reference artifact checker excludes vendor entries from its
   Java 17-owned-class check.

## Corrections

- SPEC AC10 and technical/testing decisions now require strict Java 17 compile
  targets for every example/browser-test Java source. T20-T22 own JSpecify
  migration, strict targets and source inventories; T25/check.sh builds them.
  R03/R04/R05 ownership and validation interfaces were reconciled in TASKMAP.md.
- SPEC constraints/AC03/D03 and packaging/testing decisions now define bytecode
  scope. T16-T17 require Java 17 owned/runtime/generated entries and separately
  identified Java 21-compatible processor dependencies, with no whole-archive
  exemption. T18 requires real isolated processor execution on Java 21.
- Exact plan graph, R/AC ownership and local links passed validation after edits.
