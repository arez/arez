# Public consumers use publication archives

Public JVM imports and processor plugins use release binaries; public J2CL
libraries use release source archives. This makes Bazel consumers use the same
packaging, generated sources and native JavaScript as Maven consumers. Aliases to
internal source targets would bypass that boundary and let packaging defects
escape downstream checks.
