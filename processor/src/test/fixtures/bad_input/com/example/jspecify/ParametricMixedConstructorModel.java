package com.example.jspecify;

import arez.annotations.*;
import java.util.List;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ParametricMixedConstructorModel<T extends @Nullable Object> {
  @NullUnmarked
  ParametricMixedConstructorModel(String incoming) {}

  @Observable abstract List<T> getValues();
  abstract void setValues(List<T> values);
}
