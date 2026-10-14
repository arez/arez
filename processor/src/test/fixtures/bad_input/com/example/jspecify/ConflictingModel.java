package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class ConflictingModel {
  @Observable abstract @Nullable @NonNull String getValue();
  abstract void setValue(@Nullable String value);
}
