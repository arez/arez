package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ExplicitParametricContainerModel<T extends @Nullable Object> {
  @NullUnmarked
  ExplicitParametricContainerModel(String incoming) {}
  @Observable abstract java.util.List<@Nullable T> getValues();
  abstract void setValues(java.util.List<@Nullable T> values);
  @Observable abstract java.util.List<@NonNull T> getNonNullValues();
  abstract void setNonNullValues(java.util.List<@NonNull T> values);
}
