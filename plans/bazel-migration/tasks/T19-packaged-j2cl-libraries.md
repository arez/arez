# T19 — Compile public browser libraries from release sources

- Status: complete
- Blocked by: T18
- Spec coverage: R03, R09, R13, R15, AC03, AC09, AC13, AC15

## Delivers

Core, promise, DOM, spytools and Persist browser-capable public J2CL targets compile complete release sources.

## Acceptance criteria

- [x] Pin the relevant Rose J2CL/rules_closure/rules_webtesting/JsInterop dependencies and required Java 21 compiler patch without copying the Node runner or app-specific wrappers.
- [x] Expose core-j2cl, promise-j2cl, dom-j2cl, spytools-j2cl and persist-core-j2cl labels using release source archive inputs.
- [x] Import JSpecify annotation bytecode for J2CL; remove the obsolete JSpecify GWT source overlay/filter and preserve native configuration companions.
- [x] Retain documented GwtIncompatible stripping for JVM configuration/logging/test utilities; do not delete needed native selection behavior.
- [x] Carry promise/spytools compile smoke entrypoints and compile Persist browser/web-storage sources. Declare external source/runtime dependencies explicitly.

## Validation

- bazel build //:core-j2cl //:promise-j2cl //:dom-j2cl //:spytools-j2cl //:persist-core-j2cl — verifies public release-source consumers.
- Build both retained promise/spytools smoke applications — preserves existing compilation coverage.
- Inspect source inputs and J2CL compiler runtime — proves publication parity, native companions and Java 21 binding.

## Evidence

- All five public release-source libraries and both retained ADVANCED smoke applications build successfully. Both `_dev` smoke applications also build. The sources contain 154 core, 3 promise, 17 DOM, 12 spytools and 29 Persist Java files, plus all four native JavaScript companions.
- [Action/source evidence](../T19-j2cl-inputs.json) asserts that every public J2CL action consumes its assembled release source jar and every Java compiler action starts the pinned Java 21 executable. JSpecify is a bytecode import; its obsolete GWT source overlay is deleted. JVM-only marker declarations remain documented and stripped by J2CL.
- Pinned Rose's J2CL commit `3995891275f54114e9dbfd5a8ceff97bf10030aa`, Closure 0.17.0, webtesting 0.4.1 and JsInterop base 1.2.0. Applied the reviewed compiler/test/runfiles patches without the Node runner, app-specific wrappers or ibazel patch. The observed macOS SDK metadata failure required Rose's rules_cc 0.2.25 SDK-root patch.
- A root-owned, checksummed shared Maven lock includes J2CL/Closure/JsInterop/Protobuf contributions. Without it, Protobuf's transitive partial lock omitted browser tool targets. The pinned J2CL requires its explicit JSpecify support flag; owned archive inputs already pass the Java 17 Error Prone/NullAway gate before packaging.
- `bazel build //:java_format_check`, `bazel run //tools/buildifier:buildifier_check`, plan validation, documentation links and `git diff --check` pass.
- Rebuilt all 32 release artifacts and aggregate API docs in the independent output base. [Artifact evidence](../T19-artifacts.json) records matching hashes and a passing isolated Java 21 processor/runtime consumer.
