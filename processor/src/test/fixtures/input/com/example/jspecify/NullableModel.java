package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NullableModel {
  @Observable abstract @Nullable String getValue();
  abstract void setValue(@Nullable String value);
}
