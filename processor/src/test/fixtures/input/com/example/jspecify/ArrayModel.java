package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ArrayModel {
  @Observable abstract @Nullable Object @Nullable [] getOptionalArray();
  abstract void setOptionalArray(@Nullable Object @Nullable [] value);
  @Observable abstract @Nullable Object[] getOptionalElements();
  abstract void setOptionalElements(@Nullable Object[] value);
  @Observable abstract Object @Nullable [] getOptionalContainer();
  abstract void setOptionalContainer(Object @Nullable [] value);
}
