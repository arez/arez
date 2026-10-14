# T07 — Apply and check Palantir formatting

- Status: pending
- Blocked by: T06
- Spec coverage: R05, AC05

## Delivers

Maintained Java source has a reproducible formatter and an isolated mechanical formatting commit.

## Acceptance criteria

- [ ] Port the reference formatter rule/pinned runtime and tools/java_format.sh check/write commands.
- [ ] Format maintained product, test and tool Java only. Fixture inputs and golden expected trees retain their compiler/generator-specific formatting contract.
- [ ] Keep runtime/processor behavior and golden content unchanged; inspect the mechanical diff before committing.
- [ ] Check Bazel ownership/source lists still enumerate the formatted sources.

## Validation

- tools/java_format.sh check — verifies maintained-source formatting.
- bazel test //:jvm_tests --test_output=errors and bazel build //doc-examples — confirms formatting preserved baseline behavior.
- git diff --check plus a semantic diff inspection — rules out incidental edits.

## Evidence

- pending
