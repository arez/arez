# Durable knowledge classification

Current owners were searched under docs/specs, docs/adr, docs/glossary and
docs/deferred; no overlapping active artifact exists. The resolved processor QA
question already has its owner in docs/development.md. Existing API Javadoc and
authored documentation continue to own unchanged runtime behavior.

| Candidate | Owner | Qualification / replacement test |
| --- | --- | --- |
| R01; unchanged part of R02; D01 module coverage | Retained tests and public API documentation | Test inventory/delivery scope is implementation evidence; existing runtime contracts already have durable API owners. |
| R02 nullness cut, R04; D05–D06 | docs/specs/processor-nullability.md | Accepted, independently verifiable declaration/type-use/inheritance behavior must survive a replacement processor. Its complete interpretation currently has no canonical contract owner beyond tests/code. |
| R03 Java compatibility; D03 | docs/development.md#packaged-downstream-targets | The existing durable owner state Java 17 output and JDK 21 processor compatibility; no overlapping specification is needed. |
| R03 pins; D04 and mutable technical pins | MODULE.bazel, dependency locks and Java rules | Exact tool/dependency selections and execution Java 25 are mutable implementation configuration. |
| R05; D07–D08 | Java checker/formatter rules and docs/development.md | The checker list, assertion model, formatting policy and narrow exceptions are implementation policy with existing contributor documentation. |
| R06; D09 | docs/development.md#development-checks | Golden grouping, two exact output sets and focused update ownership have a durable owner. Alternative grouping is easy to change, so no ADR qualifies. |
| R07 publication outputs; D11 | docs/releasing.md | The existing owner specifies artifact identities, separate complete archives and publication verification. |
| R08 public consumer interfaces; D12 | docs/development.md#packaged-downstream-targets | The existing owner specifies binary/source inputs, processor isolation, Java baselines and public labels. |
| D11–D12 boundary rationale | docs/adr/0002-publication-artifact-boundary.md | Public imports compiling assembled archives are surprising beside internal source targets; changing the boundary affects public consumers and packaging guarantees; direct aliases were a real alternative. |
| R09 and R15 browser support; D10, D18 | docs/project_setup.md#configure-a-j2cl-application and marker Javadoc | Supported J2CL interfaces, source inputs, GWT retirement and the marker-name convention have durable owners. Transitive third-party tools are implementation dependencies. |
| R10; D13–D14 | Browser tests and docs/development.md#local-browser-examples | Eight example cases, deterministic API controls, both Closure modes and local preview are tested implementation/development contracts. |
| R11; D15 | Authored docs, website/navigation.txt and docs/development.md#documentation-and-releases | Existing routes/content/API identities remain their authored owners; rendering, SVG/snippet directives and preview mechanics have a durable development owner. A small replaceable renderer does not meet ADR cost/surprise tests. |
| R12; D16 | docs/releasing.md and release command stubs | Manual operations/credentials are mutable operational instructions with a durable owner; real publication is outside this delivery authority. |
| R13; D17 | tools/ahab baseline/specs/exceptions and docs/development.md#complete-verification-and-coverage | Exact analysis declarations and eight browser exceptions are current tool configuration with reasons, not independent domain requirements. |
| R14; D19 | tools/check.sh, CI and docs/development.md#complete-verification-and-coverage | Gate composition and nonempty coverage reporting are contributor policy, with no percentage threshold. |
| R15 technology choice | docs/adr/0001-bazel-build.md | Replacing the whole generated-Java/browser/site build is costly; Bazel in a Java library benefits from rationale; retaining multiple toolchains or moving to a JVM-only build were real alternatives. |
| R16 | README, AGENTS, CONTRIBUTING, setup/development/releasing docs and changelog | Mutable contributor/setup guidance and user-facing migration notes belong to existing owners. |
| R17; D02, D20 | This temporary plan, task history and workflow review | Sequencing/checkpoints/reviewer identities are delivery state and do not constrain a future conforming implementation. |
| Persist Object restore, nested sidecar name and geolocation speed defects | TODO.md#known-bugs | These are observed incorrect behavior, not unresolved design questions; record concrete bugs without creating unapproved deferrals. |
| Unbound ONE inverse during construction | docs/at_reference.md#inverse and processor tests | Preserve cardinality and describe the existing construction limitation; do not turn incidental generated storage into a new public contract. |

Specification audit: each sentence in the nullability specification states an
accepted processor obligation independent of helper layout, tool pins, task
sequence or golden grouping. JSpecify annotation names and source/compiled
equivalence constrain interoperability; these exact details pass replacement.
No glossary-owned terms or essential use-case candidates were introduced. No new
question was intentionally deferred.
