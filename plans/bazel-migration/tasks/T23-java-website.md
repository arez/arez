# T23 — Generate and preview the website with Java

- Status: pending
- Blocked by: T17, T18
- Spec coverage: R11, R15, AC11, AC15

## Delivers

Arez documentation builds and previews without Docusaurus/Node while preserving its route/content/API contract.

## Acceptance criteria

- [ ] Port the small Sting Java site action/tool, CommonMark/GFM rendering, templates/navigation and local server with declared docs/assets/example-source/API inputs.
- [ ] Adapt Arez's SVG include directives in addition to file_content slicing/elision and Java API links.
- [ ] Preserve the baseline public document route/navigation inventory, assets, logo/favicon and valid content; migrate GWT-specific instructions to supported J2CL setup.
- [ ] Resolve snippets from compiled documentation transitive source jars, including generated examples; preserve /api paths.
- [ ] Add tools/site.sh build/check/serve and local fragment/asset checks; keep browser assets local and do not fetch external page links.
- [ ] Keep internal plans/domain artifacts out of published navigation; do not publish browser demos.

## Validation

- bazel build //website:site then tools/site.sh build and tools/site.sh check — proves declared generation and local-link checks.
- Compare generated routes against the recorded baseline inventory and inspect representative API/snippet/SVG pages.
- Run tools/site.sh serve and inspect homepage, navigation, a long document, mobile layout and /api in a browser.
- Run the site build with host Node unavailable — demonstrates the replacement's prerequisite contract.

## Evidence

- pending
