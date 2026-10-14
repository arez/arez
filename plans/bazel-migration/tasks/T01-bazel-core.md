# T01 — Build and test core with Bazel

- Status: complete
- Blocked by: None
- Spec coverage: R01, R02, R03, R06, R17, AC01, AC02, AC03, AC06, AC17

## Delivers

A checked-in Bazel graph builds core and runs its existing TestNG suite with the current semantic contract.

## Acceptance criteria

- [x] Add MODULE.bazel/.bazelrc/dependency generation, explicit package BUILD files and the reference Java/TestNG wrappers under the initial baseline compiler policy.
- [x] Pin Bazel 9.3.0/toolchains and required dependency checksums. Initially keep the current Proton and annotations where needed; the final pin/cut belongs to T09.
- [x] Expose //core and //core:tests. Carry assertions/development properties and JSON diagnostic fixtures as declared data.
- [x] Adapt JDepend to declared bytecode archives or test-local extraction rather than a checkout compile directory; preserve its dependency assertions.
- [x] Record the core test/provider inventory and baseline results. Add Buildifier fix/check in its dedicated tools package and Bazel output ignores.

## Validation

- bazel build //core — proves core compilation and declared processors/resources.
- bazel test //core:tests --test_output=errors — runs all retained core cases, including JDepend/invariant JSON.
- bazel run //tools/buildifier:buildifier_check — checks the initial graph.

## Evidence

- Approved plan commit: 3ce61e840aeb6d9fd6cc15dd193a201736a499ed.
- bazel build //core passed, compiling all 154 core sources with the declared Grim processor.
- bazel test //core:tests --test_output=errors passed: 843 TestNG tests, 843 passes, 0 failures, 0 skips across 75 selected classes. The complete 84-source inventory is in ../core-test-inventory.json.
- JDepend consumed the declared core bytecode jar directly using its existing archive support; no extraction helper or architecture assertion changes were needed. Invariant JSON remained declared, checked runfile data.
- bazel run //tools/buildifier:buildifier_check passed after replacing deprecated location variables with rootpath.
- bazel test //third_party/java:verify_config_sha256 passed; tools/update_java_deps.sh generated checksum-pinned repositories/imports using JDK 21 and the pinned depgen jar. bash -n passed.
- Archive inspection found 169 class entries, all Java 17 (major 61), and both native JS/configuration resources. Every inventory test class is explicitly selected.
- Guiceyloops exports only the JDK-only ValueUtil used by core tests; unused embedded-server transitive dependencies were excluded after inspecting its source. Depgen's existing exported processor adapter is used directly.
