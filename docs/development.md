# Developing Arez

- [Get started](#get-started)
- [Make and submit a change](#make-and-submit-a-change)
- [Processor goldens](#processor-goldens)
- [Runtime fixtures and invariant codes](#runtime-fixtures-and-invariant-codes)
- [Local browser examples](#local-browser-examples)
- [Packaged downstream targets](#packaged-downstream-targets)
- [Build the documentation](#build-the-documentation)
- [Releases](#releases)

## Get started

Install Bash, Python 3, and Bazelisk (or the Bazel version in
[`.bazelversion`](../.bazelversion)). Bazel downloads the pinned Java toolchains.
Runtime and generated application classes target Java 21; running the published
annotation processors requires JDK 21 or later.

Run commands from the repository root. To build the core library and processor
and run the JVM tests:

```sh
bazel build //core //processor
bazel test //:jvm_tests
```

## Make and submit a change

For substantial changes, open an issue to discuss the scope and approach before
implementation. Submit changes through a pull request; all submissions require
review before merging.

Format maintained Java with `tools/java_format.sh write` and review the diff.
Processor fixtures use the [golden updater](#processor-goldens) instead. List new
sources explicitly in the owning `BUILD.bazel` file.

Run the tests for the area you change. For processor work and formatting checks:

```sh
bazel test //processor:all_tests
bazel test //persist/processor:all_tests
tools/java_format.sh check
bazel run //tools/buildifier:buildifier_check
```

Use JSpecify `@NullMarked` scopes and type-use `@Nullable` annotations for
nullability contracts. Public APIs need Javadoc. Add tests for changed behavior
and update the documentation when APIs change.

Runtime builds and generated-output tests enforce Error Prone and JSpecify-mode
NullAway. Javac warnings fail the build; diagnostic tests use plain javac. JVM
tests enable assertions. See the [processor nullability
specification](specs/processor-nullability.md) when changing nullness contracts.

Describe user-visible changes under `Unreleased` in [CHANGELOG.md](../CHANGELOG.md).
This includes processor errors, warnings, and compile-time validation changes.
Release preparation adds the `Changes in this release:` heading automatically.

### Complete verification and coverage

Before submitting, run:

```sh
tools/check.sh
```

This is the [CI entrypoint](../.github/workflows/bazel-j2cl.yml). It checks
formatting, build hermeticity, JVM tests, browser tests in development and
optimized production modes, packaged consumers, coverage, and the website.
`//:all_tests` collects JVM, browser, and site tests; it does not replace the full
check script.

JVM coverage writes `reports/coverage/jvm.lcov` and `reports/coverage/summary.json`.
The summary reports production Java line and branch coverage separately for
`core`, `processor`, `extras/dom`, `extras/spytools`, `persist/core`, and
`persist/processor`, plus their combined totals. Test support and fixtures do
not contribute to these percentages. Every production Java source must appear
in the report, including sources with no executable lines.

`tools/check_coverage.py` enforces independent line and branch regression floors
for each module. A strong module cannot compensate for a regression in another.
Raise the floors in `MINIMUMS` when tests improve coverage; the target for core
and processor is at least 98% lines and 90% branches. Compare unrounded
percentages against the floors; displayed percentages are rounded to two places.
The JSON summary is retained even when a percentage gate fails.

These are JVM measurements. Browser tests run separately in development and
optimized modes and do not contribute coverage. JavaScript-only implementations
and unexercised assertion outcomes remain visible in the JVM totals; do not
exclude them or add tests of impossible states merely to meet a percentage.
CI retains the coverage and artifact reports under `reports/`.

Ahab checks whether build actions declare their inputs and tools. Run
`bazel run //tools/ahab:hermeticity.check` to enforce the baseline or
`bazel run //tools/ahab:hermeticity` to inspect the graph. Review findings before
changing the baseline, exceptions, or reproducibility declarations in
`tools/ahab/`; `:hermeticity.explain` explains the baseline and
`:hermeticity.update` updates it.

## Processor goldens

Generated-output tests compare the exact emitted-file set in both `expected` and
`expectedFormatted`. Run the family that owns the changed fixture, then update
its goldens:

```sh
bazel test //processor/src/test/java/arez/processor:observable_goldens
tools/update_goldens.sh arez observable
tools/update_goldens.sh persist types
```

The family names and fixture membership are declared in the [Arez test
package](../processor/src/test/java/arez/processor/BUILD.bazel) and [Persist test
package](../persist/processor/src/test/java/arez/persist/processor/BUILD.bazel).
Diagnostic expectations stay beside their Java test cases. The aggregate suites
also run warning and forked-javac formatter checks.

The updater regenerates both modes in a temporary directory, copies changed
outputs back and reruns the selected family. Declare new golden paths in the
owning BUILD file first. Review the generated diff before committing.

## Runtime fixtures and invariant codes

```sh
tools/update_runtime_fixtures.sh core-invariants
tools/update_runtime_fixtures.sh integration arez.integration.NativeComponentsIntegrationTest.inspectComponentViaSpy
```

The core updater runs the entire suite to collect invariant messages. The
integration updater runs one spy-event scenario and updates only its JSON file.
Both rerun their checks after copying the results back.

Use `python3 tools/error_codes.py unused`, `new` or `next` to inspect or allocate
invariant codes. The commands use the maintained message inventory; `next` reuses
a retired code before allocating a new one.

## Local browser examples

```sh
python3 tools/browser/preview.py //extras/promise-example:app
python3 tools/browser/preview.py //extras/promise-example:app --production
bazel test //extras/promise-example:browser_test
bazel test -c opt //extras/promise-example:browser_test
```

The preview listens on loopback port 8000; use `--port` to choose another port.
Default builds enable development checks. `--production` selects the optimized
production application; `-c opt` selects the same mode for Bazel tests.

The DOM examples use targets under `//extras/dom-example`:

| Example | Preview target | Browser test |
| --- | --- | --- |
| Browser location | `:location_app` | `:location_browser_test` |
| Window size | `:size_app` | `:size_browser_test` |
| Media query | `:media_app` | `:media_browser_test` |
| Document visibility | `:visibility_app` | `:visibility_browser_test` |
| Geolocation | `:geo_app` | `:geo_browser_test` |
| Idle status | `:idle_app` | `:idle_browser_test` |
| Network status | `:network_app` | `:network_browser_test` |

For example, run `python3 tools/browser/preview.py //extras/dom-example:geo_app`.
JavaScript entry modules sit beside their Java examples as `.entry.js` files.
Browser tests control external APIs and time so they can run without live network
services or permissions.

Run the complete browser matrix with `bazel test //:browser_tests` and
`bazel test -c opt //:browser_tests`. Build `//extras/dom-example:java` and
`//extras/promise-example:java` to check the example Java sources directly.

## Packaged downstream targets

[Project setup](project_setup.md#configure-bazel) describes the public JVM imports,
processor plugins and J2CL libraries. They use the [publication
archives](adr/0002-publication-artifact-boundary.md), so changes to packaging must
also work for downstream consumers.

```sh
bazel build //tools/release:maven_artifacts //tools/release:site_javadoc
python3 tools/release/check_artifacts.py
python3 tools/check_bazel_consumer.py
```

These checks inspect the archives and compile and run consumers with the packaged
processors. The Bazel consumer runs in a separate workspace to check the public
labels and required root configuration. It also builds a J2CL consumer, checks
the exact public target inventory, and verifies that internal targets and
Starlark modules reject downstream access.

## Build the documentation

`tools/site.sh build` writes `reports/site`; `tools/site.sh check` checks local
files and fragments. `tools/site.sh serve` builds and serves the site on loopback
port 3000. Rebuild and refresh after editing documentation. Source snippets come
from the compiled examples, including generated Java. External links are not
checked by the site tool.

## Releases

Release all Arez artifacts together under `org.realityforge.arez`. Each artifact
has a binary jar, sources jar, Javadoc jar and POM. Run the
[complete verification](#complete-verification-and-coverage) before preparing a
release.

Publication also requires Git, GPG, curl, and the GitHub CLI. Configure a Central
Portal user token through `MAVEN_CENTRAL_USERNAME` and `MAVEN_CENTRAL_PASSWORD`.
GPG uses its default signing key unless `GPG_USER` selects another key;
`GPG_PASS` supplies a passphrase through standard input. Keep credentials outside
the repository.

### Prepare and inspect

Choose the numeric release version, then prepare the notes and build an unsigned
bundle:

```bash
read -r -p 'Release version: ' RELEASE_VERSION
python3 tools/release/prepare.py "$RELEASE_VERSION"
tools/release/package.sh "$RELEASE_VERSION" --unsigned
python3 tools/release/verify_bundle.py "dist/arez-$RELEASE_VERSION.zip" "$RELEASE_VERSION" --unsigned
tools/site.sh build
tools/site.sh check
```

Preparation moves the current changelog notes into a dated release section,
leaves an empty `Unreleased` section and updates the Arez version in
`docs/project_setup.md` and `persist/README.md`. Review and commit those changes
on `master` before publishing.

The bundle contains the artifacts and checksums in Maven coordinate directories.
The verifier checks the version, coordinates, contents and checksums. An unsigned
bundle is for inspection; the upload command requires a signed bundle.

### Publish

Run each command only after the previous one succeeds. Keep `RELEASE_VERSION` set
to the version prepared above. These commands create remote tags, publish to
Maven Central and GitHub, and deploy the website:

```sh
git tag "v$RELEASE_VERSION"
git push origin master
git push origin "v$RELEASE_VERSION"
tools/release/package.sh "$RELEASE_VERSION"
tools/release/upload_maven_central.sh "$RELEASE_VERSION"
tools/release/github_release.sh "$RELEASE_VERSION"
tools/site.sh deploy "$RELEASE_VERSION"
```

Packaging signs the artifacts and includes signatures and their checksums. The
Central upload verifies the bundle and requires HEAD to have the exact release
tag on the default branch, with the remote default branch included in its
history. It uses the [Central Portal API](https://central.sonatype.org/publish/publish-portal-api/)
for automatic publication and waits for success or reports the deployment error.

The GitHub release uses the remote tag and that version's changelog notes. Site
deployment rebuilds and checks the site, replaces the published files in
`arez/arez.github.io`, and commits and pushes the result.

Add subsequent changes under `Unreleased`. To resume local artifact checks after
packaging a release version, rebuild the default snapshot artifacts:

```sh
bazel build //tools/release:maven_artifacts //tools/release:site_javadoc
```
