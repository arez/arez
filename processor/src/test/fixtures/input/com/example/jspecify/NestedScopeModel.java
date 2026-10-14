package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NestedScopeModel {
  @NullUnmarked
  @Observable abstract @Nullable String getValue();
  @NullUnmarked
  abstract void setValue(@Nullable String value);
}
