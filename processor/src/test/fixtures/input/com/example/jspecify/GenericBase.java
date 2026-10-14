package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponentLike
abstract class GenericBase<T extends @Nullable Object> {
  @Observable public abstract T getValue();
  public abstract void setValue(T value);
}
