# Build the complete project with Bazel

Arez uses Bazel for annotation processing, JVM tests, J2CL applications,
documentation and publication archives so they share declared dependencies and
cached build results. Separate Java, browser and site builds would duplicate
dependency configuration and require another layer to coordinate their outputs.
