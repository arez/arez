# Releasing Arez

Release all Arez artifacts together under `org.realityforge.arez`. Each artifact
has a binary jar, sources jar, Javadoc jar and POM. Run `tools/check.sh` before
preparing a release.

Local packaging requires Bazel, Bash and Python 3. Publication also requires Git, GPG,
curl and the GitHub CLI. Configure a Central Portal user token through
`MAVEN_CENTRAL_USERNAME` and `MAVEN_CENTRAL_PASSWORD`. GPG uses its default signing
key unless `GPG_USER` selects another key; `GPG_PASS` supplies a passphrase through
standard input. Keep credentials outside the repository.

## Prepare and inspect

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

## Publish

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
