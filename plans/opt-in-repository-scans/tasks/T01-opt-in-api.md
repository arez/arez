# T01 — Implement the opt-in API and migration

- Spec coverage `R1`, `R2`, `R3`, `AC1`, `AC2`, `AC3`

Replace the base scans using the mapping in the spec. Migrate the repository tests and documentation example. Record the source and binary break and show how a concrete repository exposes a scan. Capture the architectural rationale.

## Acceptance and validation

- Check every helper's visibility and the absence of the former public methods. Exercise positive cross-package opt-in calls and a lookup-only repository.
- Reuse the repository behavior tests for result and failure paths. Add focused coverage for membership observation at stream creation and the read guard, and check configured list wrapping.
- Run `bazel test //:jvm_tests`, `bazel build //doc-examples`, Java format checks, and the domain documentation link validator. Inspect the diff and migrations.

## Evidence

- Pending.
