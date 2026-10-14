# T01 — Build and test core with Bazel

- Status: pending
- Blocked by: None
- Spec coverage: R01, R02, R03, R06, R17, AC01, AC02, AC03, AC06, AC17

## Delivers

A checked-in Bazel graph builds core and runs its existing TestNG suite with the current semantic contract.

## Acceptance criteria

- [ ] Add MODULE.bazel/.bazelrc/dependency generation, explicit package BUILD files and the reference Java/TestNG wrappers under the initial baseline compiler policy.
- [ ] Pin Bazel 9.3.0/toolchains and required dependency checksums. Initially keep the current Proton and annotations where needed; the final pin/cut belongs to T09.
- [ ] Expose //core and //core:tests. Carry assertions/development properties and JSON diagnostic fixtures as declared data.
- [ ] Adapt JDepend to declared bytecode archives or test-local extraction rather than a checkout compile directory; preserve its dependency assertions.
- [ ] Record the core test/provider inventory and baseline results. Add Buildifier fix/check in its dedicated tools package and Bazel output ignores.

## Validation

- bazel build //core — proves core compilation and declared processors/resources.
- bazel test //core:tests --test_output=errors — runs all retained core cases, including JDepend/invariant JSON.
- bazel run //tools/buildifier:buildifier_check — checks the initial graph.

## Evidence

- pending
