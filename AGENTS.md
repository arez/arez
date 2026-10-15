# Repository Guidelines

This guide helps contributors work effectively on the Arez codebase.

## User Interaction

When asked to perform a task, ask the user questions one at a time until you have enough context. Feel free to make
reasonable assumptions based on patterns present in the code and ask the user to confirm the assumptions if there are
reasonable alternatives.

## Project Structure & Module Organization

- Java modules:
  - `core/` (runtime and annotations)
  - `processor/` (annotation processor)
  - `extras/dom` (Arez-Enabled DOM components) and `extras/dom-example` (Examples for the DOM components)
  - `extras/promise` (Arez-Enabled Promise component) and `extras/promise-example` (Examples for the promise components)
  - `extras/spytools` (Spy support infrastructure)
  - `extras/testng` (TestNG support infrastructure)
  - `persist/core` and `persist/processor` (persistence runtime and processor)
- Tests and verification modules:
  - `integration-tests/` (integration tests)
  - `persist/integration-tests/` (Persist integration tests)
- Source code used in documentation: `doc-examples/`.
- Docs and site: `docs/`, `website/` (Java-generated static site), `assets/`.
- Build configuration: `MODULE.bazel`, owning-package `BUILD.bazel` files and `tools/`. List owned sources explicitly.
- Source layout: `*/src/main/java/...`; tests: `*/src/test/java/...`.
- Generated binaries and build artifacts should stay out of version control and should stay untouched unless you are
  troubleshooting a local build.
- Keep `README.md` and `docs/` aligned with new features so downstream teams stay informed.

## General Principles

- Readability: Write code that is easy to read and understand. Prioritize clarity over overly clever or obscure
  solutions.
- Consistency: Strive for consistency in naming, formatting, and architectural patterns throughout the project.
- Simplicity (KISS): Keep It Simple, Stupid. Avoid unnecessary complexity.
- Don't Repeat Yourself (DRY): Avoid code duplication. Utilize functions, classes, and reusable components.
- Commenting:
    - Comment code that is complex, non-obvious, or critical.
    - Explain why something is done, not just what is being done (if the what is clear from the code).
    - Keep comments up-to-date with code changes.
- Modularity: Design components to be as self-contained and reusable as possible.
- Performance: Be mindful of performance implications, especially for real-time operations. Profile and optimize
  critical code paths.
- Error Handling: Implement robust error handling and provide clear feedback to users or logs when errors occur.

## Build, Test, and Development Commands

Prerequisites: Bazel 9.3.0 (or Bazelisk), Bash and Python 3. Bazel downloads pinned Java 21/25 runtimes;
application and owned processor bytecode target Java 17. Running published annotation processors requires JDK 21+.

- Full local/CI gate: `tools/check.sh` (format, Buildifier, Ahab, JVM/browser tests, artifacts, coverage and site).
- Focused JVM tests: `bazel test //:jvm_tests`; complete tests: `bazel test //:all_tests`.
- Browser matrix: `bazel test //:browser_tests` and `bazel test -c opt //:browser_tests`.
- Docs: `tools/site.sh build`, `tools/site.sh check`, `tools/site.sh serve`; output is `reports/site`.
- For processor fixtures, source updates or public consumer targets, read [development](docs/development.md).
- For signing, publication or deployment, read [releases](docs/development.md#releases).

## Coding Style & Naming Conventions

- Language: Java 17; compilation uses `-Xlint:all` and `-Werror` (warnings must be fixed).
- Maintained Java uses Palantir formatting: `tools/java_format.sh write` and `tools/java_format.sh check`.
  Fixture inputs and their generated goldens retain their declared formatting modes; review their exact output changes.
- Nullability: JSpecify `@NullMarked` scopes and type-use `@Nullable`/`@NonNull`; legacy annotations have no effect.
  For processor nullness changes, read the [nullability specification](docs/specs/processor-nullability.md). Use `final` where practical.
- Naming: packages lowercase (`arez.*`), classes `PascalCase`, methods/fields `camelCase`, constants `UPPER_SNAKE_CASE`.
- Public API must include Javadoc; keep package-level docs in `package-info.java`.

## Testing Guidelines

- Framework: TestNG for JVM suites; Closure jsunit with pinned Chromium for browser suites.
- Location: place tests under `*/src/test/java`; integration tests live in `integration-tests/src/test/java`.
- Naming: suffix unit tests with `Test` and integration tests with `IntegrationTest`.
- Run `tools/check.sh` before submitting. Keep every retained scenario in its owning aggregate; generated-output
  tests compare exact emitted-file sets in both modes. Run focused checks while iterating, then the full gate.

## Commit & Pull Request Guidelines

- Follow `CONTRIBUTING.md` and the Code of Conduct.
- Commits: small, focused, imperative subject; reference issues where relevant; update `CHANGELOG.md` for user-visible changes.
- When updating `CHANGELOG.md`, add the message under the "Unreleased" section. DO NOT add a `Changes in this release:` header as that is added as part of the automation.
- Treat annotation-processor errors, warnings, diagnostics, and compile-time validation changes as user-visible
  changes that require a `CHANGELOG.md` update unless the user explicitly says otherwise.
- PRs: include a clear description, linked issues, tests for behavior, and docs updates if APIs change. Add screenshots or generated artifacts when helpful.

## Security & Configuration Tips (Optional)

- Never commit secrets.
- Release credentials belong outside the checkout. Use the explicit unsigned-preview commands before publication.
- Preserve the [public artifact boundary](docs/adr/0002-publication-artifact-boundary.md): public consumers use assembled release jars/source archives.
