# Contributions and releases

## Changes and review

- Follow [CONTRIBUTING.md](../../CONTRIBUTING.md) and the
  [Code of Conduct](../../CODE_OF_CONDUCT.md).
- Keep `README.md` and `docs/` aligned with new features and API changes.
- Use small, focused commits with imperative subjects; reference issues where relevant.
- Describe PR behavior changes, link relevant issues, and include validation.
  Include screenshots or generated artifacts when they help review.

## Changelog

- Add user-visible changes under `Unreleased` in [CHANGELOG.md](../../CHANGELOG.md).
- Processor errors, warnings, diagnostics, and compile-time validation changes
  require a changelog entry unless the user explicitly says otherwise.
- Release automation adds `Changes in this release:`; leave that heading to automation.

## Packaging and releases

- Before changing public consumer targets or packaging, read the
  [publication artifact boundary](../adr/0002-publication-artifact-boundary.md)
  and [packaged downstream checks](../development.md#packaged-downstream-targets).
  Public consumers use assembled release jars and source archives.
- Before signing, publishing, or deploying, read the
  [release workflow](../development.md#releases). Use its unsigned-preview
  commands before publication and keep release credentials outside the checkout.
