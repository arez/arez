package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ParametricContainerModel<T extends @Nullable Object> {
  ParametricContainerModel(@Nullable String incoming) {}
  @Observable abstract java.util.List<T> getValues();
  abstract void setValues(java.util.List<T> values);
}
