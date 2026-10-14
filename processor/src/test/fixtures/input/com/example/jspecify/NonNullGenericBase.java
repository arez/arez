package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponentLike
abstract class NonNullGenericBase<T extends @Nullable Object> {
  @Observable public abstract @NonNull T getValue();
  public abstract void setValue(@NonNull T value);
}
