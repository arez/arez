# Component Target Validation Specification

## Purpose

Define declared-type acceptance and runtime capability checks for `@AutoObserve`,
`@ComponentDependency` and `@MappedRelationship`.

## Requirements

1. `@AutoObserve` and `@MappedRelationship` require actual non-null targets to
   implement `ComponentObservable`. `@ComponentDependency` requires actual
   non-null targets to implement `DisposeNotifier`.
2. Within each annotation's supported declarations, accept target types whose
   required capability is established at compile time, including compatible
   interfaces and Arez component configurations that guarantee the generated
   capability.
3. When the capability is not established at compile time, accept a declared
   type only when it is annotated with `@ArezComponentLike` or an annotation
   marked with `@ActAsArezComponent`, and the consuming annotation explicitly
   sets `validateTypeAtRuntime = true`. Preserve recognition of framework
   meta-annotations by the supported simple name.
4. Reject uncertain component-like declarations without runtime opt-in, and
   reject unrelated declared types with either flag value.
5. All three annotations default `validateTypeAtRuntime` to false.
6. Explicit runtime opt-in on a statically compatible declaration produces the
   warning `Arez:UnnecessaryRuntimeTypeValidation`.
7. Support member and enclosing type suppression of that warning through
   `@SuppressWarnings` and `@SuppressArezWarnings`.
8. Honor normal warning promotion to errors for that warning.
9. Resolve inherited target types in their concrete component context. Equivalent
   source and compiled declarations have equivalent validation and generation.
10. Apply the requested observation or disposal-listener registration to accepted
    non-null targets. Unsupported values must not be silently skipped, including
    when API invariants are disabled.
11. Follow framework invariant configuration for explicit capability diagnostics.
    Failure messages may differ across runtimes and build modes.
