# T19 — Compile public browser libraries from release sources

- Status: pending
- Blocked by: T18
- Spec coverage: R03, R09, R13, R15, AC03, AC09, AC13, AC15

## Delivers

Core, promise, DOM, spytools and Persist browser-capable public J2CL targets compile complete release sources.

## Acceptance criteria

- [ ] Pin the relevant Rose J2CL/rules_closure/rules_webtesting/JsInterop dependencies and required Java 21 compiler patch without copying the Node runner or app-specific wrappers.
- [ ] Expose core-j2cl, promise-j2cl, dom-j2cl, spytools-j2cl and persist-core-j2cl labels using release source archive inputs.
- [ ] Import JSpecify annotation bytecode for J2CL; remove the obsolete JSpecify GWT source overlay/filter and preserve native configuration companions.
- [ ] Retain documented GwtIncompatible stripping for JVM configuration/logging/test utilities; do not delete needed native selection behavior.
- [ ] Carry promise/spytools compile smoke entrypoints and compile Persist browser/web-storage sources. Declare external source/runtime dependencies explicitly.

## Validation

- bazel build //:core-j2cl //:promise-j2cl //:dom-j2cl //:spytools-j2cl //:persist-core-j2cl — verifies public release-source consumers.
- Build both retained promise/spytools smoke applications — preserves existing compilation coverage.
- Inspect source inputs and J2CL compiler runtime — proves publication parity, native companions and Java 21 binding.

## Evidence

- pending
