# T26 — Remove old infrastructure and document delivered contracts

- Status: complete
- Blocked by: T25
- Spec coverage: R02, R03, R04, R06, R10, R11, R12, R15, R16, R17, AC02, AC03, AC04, AC06, AC10, AC11, AC12, AC15, AC16, AC17

## Delivers

The checkout and contributor documentation expose only the delivered build/support model, with durable knowledge preserved outside the temporary plan.

## Acceptance criteria

- [x] Remove buildfile/build.yaml, Ruby tasks/config/locks, Node/Yarn configs/locks, Docusaurus scaffolding and obsolete cleanup/setup hooks after checking their replacements.
- [x] Remove downstream-test and GWT descriptors/compiler dependencies/overlay/bootstrap resources. Preserve native JS, useful assets and J2CL exclusion markers.
- [x] Update README, AGENTS.md, CONTRIBUTING.md, setup/development/IDE docs, Persist README, relevant annotation/browser Javadocs and Unreleased migration notes.
- [x] Describe Java 21 processor requirements, ignored legacy annotations, unspecified contract errors, GWT retirement, separate sources distribution, public labels and helper/release prerequisites.
- [x] Use domain-modeling gates to classify accepted durable conclusions, create/update only qualifying specifications/ADRs and canonical owners, validate links and record them in TASKMAP.md.
- [x] Confirm the processor-test deferral was resolved in T06 and no deleted-file backlinks remain. Link plan expansions to any promoted canonical artifacts.
- [x] Read writing-for-agents before editing AGENTS.md; do not weaken repository behavioral/test instructions as a shortcut.

## Validation

- Tracked-file/configuration audit for obsolete tools/support and searches for active bundle/buildr/yarn/node/GWT commands — historical prose/third-party metadata are assessed separately.
- Run the domain-modeling documentation validator against the repository root and check setup/development links.
- tools/check.sh after deletion — demonstrates replacement completeness.
- Inspect full diff for removed runtime/native resources, accidental API changes and unannounced processor diagnostics.

## Evidence

- Removed 45 tracked legacy files (T26-removed-files.json): Buildr/build.yaml, Ruby tasks/config, Node/Yarn config/lock, downstream build/statistics module, Docusaurus React/config/CSS and all remaining GWT descriptors. Native JS companions and both documented J2CL exclusion markers remain.
- `tools/check.sh` passed after deletion (`/tmp/arez-check-t26.log`): 40 default test targets, eight production browser targets, 31 coverage targets, artifact/isolated consumers, unsigned release/stubs and site checks. Coverage remains 245 sources / 11,315 exercised lines; repeat report copying succeeds. Site checks 13,910 local links, zero broken; all 35 public routes and original image/SVG/favicon hashes remain, with internal domain/development pages excluded.
- README/AGENTS/CONTRIBUTING, setup/development/releasing/IDE docs, Persist README, annotation/reference docs, browser Javadocs and Unreleased notes describe the delivered model. Maven examples use RELEASE_VERSION to avoid recommending legacy 0.256 for the new contracts; preparation updates both properties. All Maven XML blocks parse. IDE setting names were checked against official JetBrains documentation.
- Ruby invariant-code helpers are replaced by tools/error_codes.py: current inventory returns new Arez-0229, next retired Arez-0208 and the two unused codes 0208/0217. Old downstream-statistics TODOs are removed. Three observed pre-existing defects have concrete owners in TODO.md#known-bugs; ONE inverse construction behavior is documented without changing cardinality.
- Domain-modeling classification (knowledge-classification.md) routes every R/D candidate before drafting. The qualified nullability specification and two qualified ADRs have no overlapping owners; every specification sentence passed the replacement/diff audit. Mutable publication/build/browser/release guidance remains in its existing docs. No glossary/use-case candidate or new deferral was introduced.
- Canonical link validation, plan graph validation, full diff review, shell/Python/Maven syntax and active prerequisite/bootstrap audit pass. The resolved processor-test deferral has no live backlinks. Writing-for-agents preserved review/changelog/test obligations while updating AGENTS; simplify review removed stale paths/comments and kept required consumer/QA boundaries.
