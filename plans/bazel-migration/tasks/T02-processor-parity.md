# T02 — Run Arez processor regressions through Bazel

- Status: complete
- Blocked by: T01
- Spec coverage: R01, R02, R03, R06, AC01, AC02, AC03, AC06

## Delivers

The existing Arez processor and its generated-output, diagnostic and forked-javac suites run hermetically with the baseline Proton dependency.

## Acceptance criteria

- [x] Expose //processor, an internal Arez processor plugin, and //processor:all_tests without changing processor semantics.
- [x] Compile test support once; declare all fixture trees and formatter/compiler exports as inputs/runtime options.
- [x] Preserve exact emitted-file-set checks, both golden modes, warning promotion, inherited/multi-source outputs, unresolved/two-phase scenarios and forked-javac formatter failure checks.
- [x] Record case/provider/scenario inventories, not just test-class counts; preserve Sting integration fixtures and their compilerpath.

## Validation

- bazel build //processor — builds the annotation processor.
- bazel test //processor:all_tests --test_output=errors — runs all three execution contracts with existing expectations.
- Inspect runfiles/data and compare the declared fixture/case inventory with the original suites — rules out omitted provider rows.

## Evidence

- bazel build //processor passed for all 22 processor sources, with declared service registration and internal plugin.
- bazel test //processor:all_tests --test_output=errors passed: generated_output 463/463, diagnostics 677/677, forked_javac 1/1; zero failures/skips. Both golden modes and exact output-set assertions remain unchanged; all 2,264 original fixture files are explicitly declared and unmodified.
- Shared test support compiles once. The inventory in ../processor-test-inventory.json records 88 test methods, provider names/rows, source hashes and fixture counts (716 inputs, 537 bad inputs, 505 outputs in each golden mode, one unresolved input).
- In-process fixture compilation now explicitly targets Java 17 on the pinned Java 21 runtime, removing the observed unsupported-source-version warning without suppressing diagnostics. The unresolved two-phase case reuses these same options and retains its defer.unresolved variation.
- Added the test-only JSR-250 API needed for the existing unsupported-PostConstruct diagnostic; old nullability interpretation and processor behavior are unchanged.
- TestNG verbose diagnostics expose failed assertion traces; core was rerun and passed all 843 cases. Dependency config checksum verification and Buildifier check also passed.
