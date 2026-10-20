# Public consumers use publication archives

Public Arez JVM imports and processor plugins use release binaries; public Arez
J2CL libraries use release source archives. This makes Bazel consumers use the same
Arez packaging, generated sources and native JavaScript as Maven consumers. Aliases
to internal Arez source targets would bypass that boundary and let packaging defects
escape downstream checks. Shared browser dependencies reuse their owner's J2CL
targets so combining libraries does not compile the same Closure modules twice.
