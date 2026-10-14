# T17 — Complete POM, Javadoc and artifact verification

- Status: pending
- Blocked by: T16
- Spec coverage: R03, R07, R11, AC03, AC07, AC11

## Delivers

The release collection contains all 32 Maven-equivalent artifacts and complete aggregate API documentation.

## Acceptance criteria

- [ ] Create version-selected POMs with preserved coordinates, licenses, descriptions and actual runtime dependencies; both processor POMs remain self-contained.
- [ ] Build all eight Javadoc jars and aggregate site API docs, including spytools, with stable timestamps/toolchain inputs.
- [ ] Add //tools/release:maven_artifacts and release verification for contents, dependencies, services, relocations, scoped bytecode levels and native sources. Runtime/owned/generated classes must target Java 17; explicitly identified shaded processor dependencies must run on Java 21.
- [ ] Remove old direct nullability/GWT dependencies from product POMs; retain unrelated annotations only where product API/implementation actually needs them.
- [ ] Verify no vendored annotation/runtime omission makes an isolated packaged processor fail.

## Validation

- bazel build //tools/release:maven_artifacts //tools/release:site_javadoc — produces the full distribution and /api input.
- python3 tools/release/check_artifacts.py — checks all eight identities and 32 outputs.
- Build the complete collection in an independent output base and compare hashes, including Javadoc and POM outputs.

## Evidence

- pending
