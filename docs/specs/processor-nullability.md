# Processor Nullability Specification

## Purpose

Define the JSpecify contracts that Arez and Persist validate and preserve in
generated code.

## Requirements

1. Interpret JSpecify `@NullMarked`, `@NullUnmarked`, type-use `@Nullable` and
   `@NonNull` at the declaration that owns the contract. Marking a subclass does
   not change an inherited declaration's default.
2. Preserve nullness through inherited generic substitution. Equivalent source
   and compiled declarations must produce equivalent validation and generated
   behavior.
3. Distinguish nullable arrays from nullable elements, and nullable containers
   from nullable type arguments. Preserve enclosing type arguments and wildcard
   bounds when moving a contract between scopes. Preserve the conditional nullness
   of a bare type variable; do not treat it as non-null merely because it lacks
   an explicit qualifier.
4. Ignore legacy javax and JetBrains nullability annotations. Their presence
   neither specifies a JSpecify contract nor causes a special rejection.
5. Report an error when initialization or relationship generation needs a
   specified contract and nullness is unspecified. Use the declared contract for
   constructor inputs, initializer validation, setter guards and inverse
   multiplicity.
6. Report an error when a parametric initializer cannot retain its conditional
   nullness in an unmarked generated constructor. Support a marked constructor or
   an explicit nullable/non-null qualifier at that component; do not silently
   strengthen or weaken the contract.
7. Generated members preserve the effective nullness of original declarations,
   including nested qualifiers. Added observable initializer parameters preserve
   the getter's contract in the constructor's scope. Internal construction state
   must not change declared inverse cardinality.
8. Persist resolves inherited property types in the concrete component context.
   Nullability annotations do not change converter identity, and null state
   values retain the existing skip-null storage behavior.

See [project setup](../project_setup.md) for compiler/runtime prerequisites and
[development](../development.md) for the regression and golden workflow.
The [JSpecify specification](https://jspecify.dev/docs/spec/) defines the nullness
semantics used here.
