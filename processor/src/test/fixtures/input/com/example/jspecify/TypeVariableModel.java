package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class TypeVariableModel<T extends @Nullable Object> {
  @Observable abstract @Nullable T getOptional();
  abstract void setOptional(@Nullable T value);
  @Observable abstract @NonNull T getRequired();
  abstract void setRequired(@NonNull T value);
}
