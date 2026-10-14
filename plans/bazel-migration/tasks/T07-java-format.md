# T07 — Apply and check Palantir formatting

- Status: complete
- Blocked by: T06
- Spec coverage: R05, AC05

## Delivers

Maintained Java source has a reproducible formatter and an isolated mechanical formatting commit.

## Acceptance criteria

- [x] Port the reference formatter rule/pinned runtime and tools/java_format.sh check/write commands.
- [x] Format maintained product, test and tool Java only. Fixture inputs and golden expected trees retain their compiler/generator-specific formatting contract.
- [x] Keep runtime/processor behavior and golden content unchanged; inspect the mechanical diff before committing.
- [x] Check Bazel ownership/source lists still enumerate the formatted sources.

## Validation

- tools/java_format.sh check — verifies maintained-source formatting.
- bazel test //:jvm_tests --test_output=errors and bazel build //doc-examples — confirms formatting preserved baseline behavior.
- git diff --check plus a semantic diff inspection — rules out incidental edits.

## Evidence

- Pinned reference formatting rules 0.1.1, with their locked Palantir 2.93.0 runtime. `tools/java_format.sh` supports write/check/watch; the Bazel check uses declared Java target ownership.
- `tools/java_format.sh check` passed. A second write pass changed none of the 585 maintained Java files in [inventory](../format-inventory.json).
- Independent javac parsing before/after produced identical package/type-declaration AST hashes for all 585 files. Imports/comment formatting were excluded from that comparison; the full compilation/test gate separately checks import resolution. Sample product and processor diffs were inspected as mechanical formatting only.
- `bazel test //:jvm_tests --test_output=errors` passed all 22 Bazel targets / 2,271 TestNG cases; `bazel build //doc-examples` passed. No fixture or golden content changed.
- Bazel source ownership query enumerates every one of the 577 maintained JVM sources. Eight browser bootstraps are formatted now; their strict Java/J2CL target ownership is added in T20–T22.
- Buildifier, Bash syntax and `git diff --check` passed.
