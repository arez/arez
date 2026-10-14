# T02 — Run Arez processor regressions through Bazel

- Status: pending
- Blocked by: T01
- Spec coverage: R01, R02, R03, R06, AC01, AC02, AC03, AC06

## Delivers

The existing Arez processor and its generated-output, diagnostic and forked-javac suites run hermetically with the baseline Proton dependency.

## Acceptance criteria

- [ ] Expose //processor, an internal Arez processor plugin, and //processor:all_tests without changing processor semantics.
- [ ] Compile test support once; declare all fixture trees and formatter/compiler exports as inputs/runtime options.
- [ ] Preserve exact emitted-file-set checks, both golden modes, warning promotion, inherited/multi-source outputs, unresolved/two-phase scenarios and forked-javac formatter failure checks.
- [ ] Record case/provider/scenario inventories, not just test-class counts; preserve Sting integration fixtures and their compilerpath.

## Validation

- bazel build //processor — builds the annotation processor.
- bazel test //processor:all_tests --test_output=errors — runs all three execution contracts with existing expectations.
- Inspect runfiles/data and compare the declared fixture/case inventory with the original suites — rules out omitted provider rows.

## Evidence

- pending
