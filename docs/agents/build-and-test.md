# Build and testing

## Sources and generated files

- List owned sources explicitly in the owning `BUILD.bazel` file.
- Keep generated binaries and build artifacts out of version control; edit them
  only when troubleshooting a local build.
- For processor fixtures and checked-in generated goldens, follow
  [processor goldens](../development.md#processor-goldens). Review exact output
  changes in both `expected` and `expectedFormatted`; tests compare the complete
  emitted-file sets.
- For runtime fixtures and invariant codes, follow
  [runtime fixture updates](../development.md#runtime-fixtures-and-invariant-codes).

## Tests

- Use TestNG for JVM suites and Closure jsunit with pinned Chromium for browser suites.
- Place tests in the owning module's `src/test/java`; integration suites live in
  `integration-tests/` and `persist/integration-tests/`.
- Suffix unit tests with `Test` and integration tests with `IntegrationTest`.
- Keep every retained scenario in its owning aggregate.
- Run focused checks while iterating, then the full gate before submitting as
  described in [AGENTS.md](../../AGENTS.md).

Read the [development guide](../development.md) for setup, processor test targets,
the browser matrix, coverage, and packaged consumer checks. For documentation
builds and local preview, use [build the documentation](../development.md#build-the-documentation).
