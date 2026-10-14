# T14 — Enforce strict checking on runtime products and tests

- Status: pending
- Blocked by: T13
- Spec coverage: R02, R05, AC02, AC05

## Delivers

Core, Persist runtime and extras product/test code pass the agreed Error Prone/NullAway policy.

## Acceptance criteria

- [ ] Activate the centralized reference checker policy and JSpecify-mode NullAway for runtime-owned compilations, with --release 17 and warnings as errors.
- [ ] Fix demonstrated defects or unnecessary code at the owning level; preserve public/reactive/persistence behavior.
- [ ] Check native JsInterop declarations and nullable browser API results; do not blanket-exclude generated/native-adjacent owned Java.
- [ ] If full policy activation exposes pending processor/generated-body failures, record them as the short T14-T15 enforcement window; leave all targets in the gate.
- [ ] Keep intentional test-pattern exceptions narrow and explained, rather than adding an old-policy switch.

## Validation

- Build all runtime/extras targets under strict checking; run core, extras and Persist core JVM test targets.
- tools/java_format.sh check — verifies checker fixes follow the new format.
- Inspect compiler action options and suppression scope — proves the configured policy actually runs.

## Evidence

- pending
