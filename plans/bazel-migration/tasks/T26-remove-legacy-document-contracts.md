# T26 — Remove old infrastructure and document delivered contracts

- Status: pending
- Blocked by: T25
- Spec coverage: R02, R03, R04, R06, R10, R11, R12, R15, R16, R17, AC02, AC03, AC04, AC06, AC10, AC11, AC12, AC15, AC16, AC17

## Delivers

The checkout and contributor documentation expose only the delivered build/support model, with durable knowledge preserved outside the temporary plan.

## Acceptance criteria

- [ ] Remove buildfile/build.yaml, Ruby tasks/config/locks, Node/Yarn configs/locks, Docusaurus scaffolding and obsolete cleanup/setup hooks after checking their replacements.
- [ ] Remove downstream-test and GWT descriptors/compiler dependencies/overlay/bootstrap resources. Preserve native JS, useful assets and J2CL exclusion markers.
- [ ] Update README, AGENTS.md, CONTRIBUTING.md, setup/development/IDE docs, Persist README, relevant annotation/browser Javadocs and Unreleased migration notes.
- [ ] Describe Java 21 processor requirements, ignored legacy annotations, unspecified contract errors, GWT retirement, separate sources distribution, public labels and helper/release prerequisites.
- [ ] Use domain-modeling gates to classify accepted durable conclusions, create/update only qualifying specifications/ADRs and canonical owners, validate links and record them in TASKMAP.md.
- [ ] Confirm the processor-test deferral was resolved in T06 and no deleted-file backlinks remain. Link plan expansions to any promoted canonical artifacts.
- [ ] Read writing-for-agents before editing AGENTS.md; do not weaken repository behavioral/test instructions as a shortcut.

## Validation

- Tracked-file/configuration audit for obsolete tools/support and searches for active bundle/buildr/yarn/node/GWT commands — historical prose/third-party metadata are assessed separately.
- Run the domain-modeling documentation validator against the repository root and check setup/development links.
- tools/check.sh after deletion — demonstrates replacement completeness.
- Inspect full diff for removed runtime/native resources, accidental API changes and unannounced processor diagnostics.

## Evidence

- pending
