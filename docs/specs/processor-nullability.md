# Processor Nullability Specification

## Purpose

Define how Arez and Persist interpret Java nullability contracts when validating
components and generating reactive or persistent state.

## Requirements

1. Interpret JSpecify `@NullMarked`, `@NullUnmarked`, type-use `@Nullable` and
   `@NonNull` at the declaration that owns the contract. Marking a subclass does
   not change an inherited declaration's default.
2. Preserve effective contracts through inherited generic substitution and
   compiled dependencies. Reading an equivalent source or compiled declaration
   must produce equivalent validation and generated behavior.
3. Distinguish nullable arrays from nullable elements, and nullable containers
   from nullable type arguments. Type-variable bounds and explicit nullness
   qualify the substituted value; a bare type variable is not automatically
   forced to a non-null value.
4. Ignore legacy javax and JetBrains nullability annotations. Their presence
   neither specifies a JSpecify contract nor causes a special rejection.
5. Diagnose unspecified nullness when initialization or relationship generation
   requires a specified contract. Explicit nullable/non-null contracts determine
   constructor inputs, initializer validation, setter guards and inverse
   multiplicity.
6. Generate JSpecify contracts that preserve the component's declared nullness
   and lifecycle behavior. Internal construction state must not change declared
   inverse cardinality.
7. Persist must resolve inherited getter/setter types in the concrete component
   context. Nullability annotations must not change converter identity; nullable
   state values retain the existing skip-null storage behavior.

See [project setup](../project_setup.md) for compiler/runtime prerequisites and
[development](../development.md) for the regression and golden workflow.
