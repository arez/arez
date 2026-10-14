# Build the complete project with Bazel

Arez combines annotation processing, generated Java, JVM tests, J2CL/Closure,
documentation and publication archives. Use one Bazel graph for these builds so
tools and inputs are declared together and cached consistently. Retaining Buildr
and a separate Node site build would preserve competing dependency and setup
paths; a JVM-only build would still need separate browser orchestration.
