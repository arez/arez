# T17 — Complete POM, Javadoc and artifact verification

- Status: complete
- Blocked by: T16
- Spec coverage: R03, R07, R11, AC03, AC07, AC11

## Delivers

The release collection contains all 32 Maven-equivalent artifacts and complete aggregate API documentation.

## Acceptance criteria

- [x] Create version-selected POMs with preserved coordinates, licenses, descriptions and actual runtime dependencies; both processor POMs remain self-contained.
- [x] Build all eight Javadoc jars and aggregate site API docs, including spytools, with stable timestamps/toolchain inputs.
- [x] Add //tools/release:maven_artifacts and release verification for contents, dependencies, services, relocations, scoped bytecode levels and native sources. Runtime/owned/generated classes must target Java 17; explicitly identified shaded processor dependencies must run on Java 21.
- [x] Remove old direct nullability/GWT dependencies from product POMs; retain unrelated annotations only where product API/implementation actually needs them.
- [x] Verify no vendored annotation/runtime omission makes an isolated packaged processor fail.

## Validation

- bazel build //tools/release:maven_artifacts //tools/release:site_javadoc — produces the full distribution and /api input.
- python3 tools/release/check_artifacts.py — checks all eight identities and 32 outputs.
- Build the complete collection in an independent output base and compare hashes, including Javadoc and POM outputs.

## Evidence

- Added version-selected POMs for all eight existing org.realityforge.arez identities, retaining project/license/developer metadata and actual API/runtime dependencies. Both processor POMs are dependency-free. JSpecify replaces direct legacy nullability dependencies; unrelated Language/MagicConstant metadata remains on core/spytools, and DOM retains its DoNotAutobox annotation dependency.
- Ported the Java Javadoc builder with Java 17 source interpretation, no timestamps, declared execution toolchain/classpath/license inputs, normalized sorted archives and complete API output. All eight Javadoc jars and aggregate `arez-api-javadoc.jar` are built; generated implementation classes remain in source jars while authored public APIs form the docs.
- `bazel build //tools/release:maven_artifacts //tools/release:site_javadoc` passed. `python3 tools/release/check_artifacts.py` passed all 32 artifacts: complete source/API files, licenses, dependency metadata, scoped bytecode, services, shading and retained native sources.
- The checker uses the declared Java 21 target runtime to compile an isolated Model with both processors, using only the two assembled shaded jars on processorpath. Formatted Arez/Persist outputs compile as Java 17 classes. An empty sourcepath prevents legacy dependency jars containing source payloads from being implicitly compiled; no internal source target or hidden processorpath dependency is available.
- Independent full collection build in `/tmp/arez-bazel-release-t16` executed the nine Javadoc actions and generated POMs. Comparing all 32 artifacts plus aggregate API docs passed. [Artifact evidence](../T17-artifact-evidence.json) records hashes, class counts, dependencies and processor/runtime verification. T16 established the independent binary/source compilation in that same separate output base.
- Version selection verified with `--//tools/release:version=9.8.7`: all POM identity and matching-core dependency checks passed using `--version 9.8.7`. Restored the default version and reran the independent hash comparison.
- Palantir, Buildifier, Python parsing, whitespace and plan validation passed. Public packaged labels and executable downstream application behavior are next in T18.
