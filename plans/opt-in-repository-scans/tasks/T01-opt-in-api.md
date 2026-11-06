# T01 — Implement the opt-in API and migration

- Spec coverage `R1`, `R2`, `R3`, `AC1`, `AC2`, `AC3`

Replace the base scans using the mapping in the spec. Migrate the repository tests and documentation example. Record the source and binary break and show how a concrete repository exposes a scan. Capture the architectural rationale.

## Acceptance and validation

- Check every helper's visibility and the absence of the former public methods. Exercise positive cross-package opt-in calls and a lookup-only repository.
- Reuse the repository behavior tests for result and failure paths. Add focused coverage for membership observation at stream creation and the read guard, and check configured list wrapping.
- Run `bazel test //:jvm_tests`, `bazel build //doc-examples`, Java format checks, and the domain documentation link validator. Inspect the diff and migrations.

## Evidence

- `bazel test //:jvm_tests --test_output=errors` passed all 35 Bazel test targets. The core suite ran 901 TestNG tests, including all 16 repository tests.
- `bazel build //doc-examples` compiled the generated opt-in repository without new diagnostics. Ran `RepositoryExample` using the Bazel runtime classpath with Java 21 and assertions enabled.
- `tools/java_format.sh check`, the domain link validator and `git diff --check` passed.
- Inspected Java scan call sites. The lone public scan call in tracked production/example Java uses the compiled concrete repository. The T02 probe exercises all seven public wrappers from another package.
