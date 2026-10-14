# T23 — Generate and preview the website with Java

- Status: complete
- Blocked by: T17, T18
- Spec coverage: R11, R15, AC11, AC15

## Delivers

Arez documentation builds and previews without Docusaurus/Node while preserving its route/content/API contract.

## Acceptance criteria

- [x] Port the small Sting Java site action/tool, CommonMark/GFM rendering, templates/navigation and local server with declared docs/assets/example-source/API inputs.
- [x] Adapt Arez's SVG include directives in addition to file_content slicing/elision and Java API links.
- [x] Preserve the baseline public document route/navigation inventory, assets, logo/favicon and valid content; migrate GWT-specific instructions to supported J2CL setup.
- [x] Resolve snippets from compiled documentation transitive source jars, including generated examples; preserve /api paths.
- [x] Add tools/site.sh build/check/serve and local fragment/asset checks; keep browser assets local and do not fetch external page links.
- [x] Keep internal plans/domain artifacts out of published navigation; do not publish browser demos.

## Validation

- bazel build //website:site then tools/site.sh build and tools/site.sh check — proves declared generation and local-link checks.
- Compare generated routes against the recorded baseline inventory and inspect representative API/snippet/SVG pages.
- Run tools/site.sh serve and inspect homepage, navigation, a long document, mobile layout and /api in a browser.
- Run the site build with host Node unavailable — demonstrates the replacement's prerequisite contract.

## Evidence

- `bazel build //website:site`, `tools/site.sh build`, `tools/site.sh check` passed; 13,911 local file/fragment links, zero broken. Source/API/SVG inputs are declared Bazel inputs.
- `bazel test //tools/site:tests` passed all 14 TestNG invocations, including SVG includes, unchanged code directives, snippet slicing/elision and public slash-containing heading fragments.
- Baseline/evidence JSON records the exact 35 public routes and 33-page navigation. Original six image/SVG/favicon asset hashes match; all 27 favicon-bundle files are also declared. Generated `Arez_MyComponent` is rendered from a compiled source jar. Internal development/plans pages and demos are excluded.
- `tools/site.sh serve` was inspected at localhost:3000: homepage, long FAQ and local aggregate API loaded without browser errors. At 390×844, navigation expanded correctly and document width equaled scroll width (390). Temporary server/tab closed.
- `PATH=/tmp/arez-node-free-t23:/usr/bin:/bin` contained Bazel/Python but no Node; `tools/site.sh build` passed. Replacement copy handles Bazel's read-only tree directory modes and repeat builds.
- `bazel build //:java_format_check //tools/buildifier:buildifier_check`, shell syntax, plan validator and canonical documentation link validator passed.
- Published setup now documents Java 17/JDK 21/JSpecify and packaged J2CL inputs; stale API links were corrected against current declarations. Maven 3.12.1 configuration was checked against Apache's official archived plugin documentation.
