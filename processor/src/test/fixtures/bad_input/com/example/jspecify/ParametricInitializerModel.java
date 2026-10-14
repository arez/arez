package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ParametricInitializerModel<T extends @Nullable Object> {
  @Observable abstract T getValue();
  abstract void setValue(T value);
}
