# T24 — Replace manual release and deployment commands

- Status: complete
- Blocked by: T18, T23
- Spec coverage: R12, R16, AC12, AC16

## Delivers

Maintainers can prepare, package, sign and publish through explicit documented commands using the new artifacts/site.

## Acceptance criteria

- [x] Port/adapt Sting's preparation, signed/unsigned package, Central upload, GitHub release and normal-commit site-deployment orchestration for Arez's eight artifacts.
- [x] Update both docs/project_setup.md and Persist README version references during preparation, preserving Unreleased conventions and correct coordinates.
- [x] Remove downstream archive/statistics/push operations from the replacement workflow.
- [x] Exercise preparation in a disposable copy and local unsigned preview bundles; verify remote-command plumbing with local stubs and synthetic credentials.
- [x] Do not run real signing/upload/tag/GitHub-release/site-deploy commands against the active project; document required release tools/credentials without exposing values.

## Validation

- Build and inspect an unsigned preview bundle from all 32 release artifacts, checksums and expected coordinate paths.
- Run preparation against a disposable copy and inspect changelog/version updates without modifying the active release state.
- Validate shell/Python syntax and exercise publish/deploy argument/error plumbing with local command stubs.
- Review the manual release instructions end to end; prove no Ruby/Node/downstream commands remain.

## Evidence

- `tools/release/package.sh 0.257 --unsigned` and `verify_bundle.py ... --unsigned` passed: 32 artifacts, 96 coordinate/checksum entries with requested POM identities. No signing tool is called for an unsigned preview.
- `prepare.py 0.257` in a disposable copy updated both Arez version properties and dated v0.257 notes while leaving Unreleased and compiler-plugin 3.12.1 intact. Repeated preparation was rejected before writes.
- `python3 tools/release/check_commands.py` passed with local stubs/synthetic credentials: 32 signature/checksum pairs, rejection of unsigned uploads, Central upload/status success and FAILED handling, missing credentials, wrong tag and snapshot rejection, GitHub notes/verify-tag and one normal `HEAD:master` site push. No actual tag, signing, upload, release or deployment occurred.
- Shell syntax/Python compilation, canonical links and Buildifier passed. `bazel build //website:site` checked all local links after setup updates. The default artifact checker passed again after restoring snapshot outputs; evidence is in T24-artifact-evidence.json.
- `docs/releasing.md` covers all replacement commands and required tools/credentials. Replacement files have no Ruby/Node/downstream operations; deployment uses ordinary commits/pushes. Central arguments/status were verified against Sonatype's official Portal API documentation.
