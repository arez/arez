# Java conventions

- Use `final` where practical.
- Format maintained Java with `tools/java_format.sh write`; verify with
  `tools/java_format.sh check`.
- Fixture inputs and generated goldens retain their declared formatting modes.
  For fixture changes, follow [build and testing](build-and-test.md).
- Use JSpecify `@NullMarked` scopes and type-use `@Nullable` / `@NonNull`;
  legacy nullability annotations have no effect. Before changing processor
  nullness contracts, read the [nullability specification](../specs/processor-nullability.md).
- Document public APIs with Javadoc and package-level APIs in `package-info.java`.

For Java target versions and enforced compiler checks, use the
[development guide](../development.md#get-started) and
[compile policy](../../third_party/java/rules.bzl).
