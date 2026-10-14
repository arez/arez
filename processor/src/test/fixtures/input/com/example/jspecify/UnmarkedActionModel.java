package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullUnmarked
@ArezComponent(sting = Feature.DISABLE)
abstract class UnmarkedActionModel extends UnmarkedActionBase {
  @Action
  String classScope(String value) { return value; }

  @NullMarked
  @Action
  String marked(String value) { return value; }

  static class NestedBase {
    @Action
    String methodScope(String value) { return value; }
  }

  @NullMarked
  @ArezComponent(sting = Feature.DISABLE)
  abstract static class Nested extends NestedBase {
    @NullUnmarked
    Nested(String incoming) {}

    @Observable
    abstract java.util.List<String @Nullable []> getValues();
    abstract void setValues(java.util.List<String @Nullable []> values);

    @NullUnmarked
    @Action
    @Nullable String explicit(@NonNull String value) { return value; }
  }
}
