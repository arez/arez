# Releasing Arez

Release all eight artifacts together under `org.realityforge.arez`. The release
contains binary, complete sources, Javadoc and POM files for core, processor,
DOM, promise, spytools, TestNG, Persist core and Persist processor.

Run the full `tools/check.sh` gate before preparing a release. Bazel and Python 3
are required for local packaging. Publishing also requires Git, GPG, curl and
the GitHub CLI. Central credentials are a Portal user token pair in
`MAVEN_CENTRAL_USERNAME` and `MAVEN_CENTRAL_PASSWORD`. Optional `GPG_USER` chooses
the signing key; `GPG_PASS` supplies its passphrase through standard input.
Keep these values outside the repository.

## Local preparation and preview

Use the next numeric version in place of `0.257` below:

```sh
python3 tools/release/prepare.py 0.257
tools/release/package.sh 0.257 --unsigned
python3 tools/release/verify_bundle.py dist/arez-0.257.zip 0.257 --unsigned
tools/site.sh build
tools/site.sh check
```

Preparation moves the current notes into a dated release section, leaves an
empty `Unreleased` section, and updates the Arez version property in both
`docs/project_setup.md` and `persist/README.md`. Review and commit those three
files. Compiler plugin versions are left unchanged.

The bundle has 32 artifacts plus MD5/SHA-1 checksum files in Maven coordinate
directories. POM identities must match the requested version. The unsigned
bundle is for local inspection; upload rejects it. To exercise publishing
plumbing locally, build the unsigned `0.257` preview and run
`python3 tools/release/check_commands.py`. It uses a disposable copy, synthetic
credentials and local Git/GPG/curl/gh/Bazel stubs.

## Publication

After reviewing and committing the prepared release on the default branch,
create and push its tag explicitly. These commands publish remote state:

```sh
git tag v0.257
git push origin master
git push origin v0.257
tools/release/package.sh 0.257
tools/release/upload_maven_central.sh 0.257
tools/release/github_release.sh 0.257
tools/site.sh deploy 0.257
```

Packaging signs all 32 artifacts and adds checksums for their signatures. The
upload command verifies bundle completeness/checksums/coordinates, requires the
exact release tag on the default branch containing the remote default branch,
and uses the [Central Portal API](https://central.sonatype.org/publish/publish-portal-api/)
with automatic publication. It polls until publication succeeds or reports the
failed deployment. It never prints credential values.

GitHub release creation uses the existing remote tag and that release's
changelog notes. Site deployment rebuilds and checks the site, clones
`arez/arez.github.io`, replaces its published files, commits changes and pushes
normally to `master`. It does not force-push. Finish subsequent development by
adding new changes under the already empty `Unreleased` section.

Local artifact verification uses the default `0.0.0-SNAPSHOT` version unless a
release version is selected. After packaging, restore that build with
`bazel build //tools/release:maven_artifacts //tools/release:site_javadoc` before
running the default artifact checker.
