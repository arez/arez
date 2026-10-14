# T14 — Enforce strict checking on runtime products and tests

- Status: complete
- Blocked by: T13
- Spec coverage: R02, R05, AC02, AC05

## Delivers

Core, Persist runtime and extras product/test code pass the agreed Error Prone/NullAway policy.

## Acceptance criteria

- [x] Activate the centralized reference checker policy and JSpecify-mode NullAway for runtime-owned compilations, with --release 17 and warnings as errors.
- [x] Fix demonstrated defects or unnecessary code at the owning level; preserve public/reactive/persistence behavior.
- [x] Check native JsInterop declarations and nullable browser API results; do not blanket-exclude generated/native-adjacent owned Java.
- [x] If full policy activation exposes pending processor/generated-body failures, record them as the short T14-T15 enforcement window; leave all targets in the gate.
- [x] Keep intentional test-pattern exceptions narrow and explained, rather than adding an old-policy switch.

## Validation

- Build all runtime/extras targets under strict checking; run core, extras and Persist core JVM test targets.
- tools/java_format.sh check — verifies checker fixes follow the new format.
- Inspect compiler action options and suppression scope — proves the configured policy actually runs.

## Evidence

- Central wrappers now apply the exact pinned Sting list of 78 explicit Error Prone checks, all built-in defaults, warnings as errors and JSpecify-mode NullAway 0.14.2. The checker plugin comes from that immutable module. [Policy evidence](../T14-policy-evidence.json) records representative javac action flags, Java 17 release, canonical JSpecify mode and assertion reasoning.
- Assertion reasoning reduced 116 initial core nullness findings to 32 without changing runtime behavior. Remaining fixes express nullable action results, lookup callback results, cached values and erased property access, freeze transaction validation's parent reference, correct nullable array elements and make utility flag constructors private. Style changes use var only where the checker requests it.
- `bazel test //core:tests //extras/spytools:tests --test_output=errors` passed: 845 core and nine spytools cases, no failures/skips. [Inventory](../T14-test-inventory.json) proves every method in the 73 changed test/support sources remains. Existing runtime diagnostic fixtures are unchanged. TestNG assertions retain their diagnostics, with explicit Java assertions making established non-null facts visible to the compiler; one invalid-null-key test has a narrow documented suppression.
- Native geolocation/JSON/console parameter contracts and nullable state-map values are checked. The legacy JsInterop property-map boundary uses explicit unchecked casts already provided by that library, retaining its runtime representation. Browser spy maps, equals parameters and reflection-mutated configuration fields have accurate contracts.
- Runtime feature-dependent spy info methods and the successful generic cache read retain small method-local documented exceptions. No generated path, native class, package or runtime product is blanket excluded.
- `bazel build //core //extras/dom //extras/promise //extras/spytools //extras/testng //persist/core --keep_going` now emits no maintained-source diagnostics. Its 131 remaining NullAway diagnostics occur only in generated code: kernel lifecycle access, nullable property mutators, generic accessor/mutator arguments and restore data. The attempted DOM/Persist runtime tests fail to build only on these generated diagnostics. T15 is their explicit owner in the agreed short enforcement window; all labels remain in the gate.
- Temporary `migration_pending_checks` entries keep processors, QA support, integrations and doc examples on the prior compiler only until T15. They are internal migration state, not a retained developer option. Source generation itself is still checked when compiled into runtime products.
- Full Palantir and Buildifier checks passed. Representative action inspection confirms strict options and no XepDisableAllChecks on core. Whitespace, plan and documentation-link checks pass; T15 must restore the full strict JVM milestone.
