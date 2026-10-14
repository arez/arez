package com.example.override_generics;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
abstract class BaseModel<T extends @Nullable Object>
{
  protected abstract void myAbstractMethod( @NonNull T value );
}
