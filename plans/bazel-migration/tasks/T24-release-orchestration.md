# T24 — Replace manual release and deployment commands

- Status: pending
- Blocked by: T18, T23
- Spec coverage: R12, R16, AC12, AC16

## Delivers

Maintainers can prepare, package, sign and publish through explicit documented commands using the new artifacts/site.

## Acceptance criteria

- [ ] Port/adapt Sting's preparation, signed/unsigned package, Central upload, GitHub release and normal-commit site-deployment orchestration for Arez's eight artifacts.
- [ ] Update both docs/project_setup.md and Persist README version references during preparation, preserving Unreleased conventions and correct coordinates.
- [ ] Remove downstream archive/statistics/push operations from the replacement workflow.
- [ ] Exercise preparation in a disposable copy and local unsigned preview bundles; verify remote-command plumbing with local stubs and synthetic credentials.
- [ ] Do not run real signing/upload/tag/GitHub-release/site-deploy commands against the active project; document required release tools/credentials without exposing values.

## Validation

- Build and inspect an unsigned preview bundle from all 32 release artifacts, checksums and expected coordinate paths.
- Run preparation against a disposable copy and inspect changelog/version updates without modifying the active release state.
- Validate shell/Python syntax and exercise publish/deploy argument/error plumbing with local command stubs.
- Review the manual release instructions end to end; prove no Ruby/Node/downstream commands remain.

## Evidence

- pending
