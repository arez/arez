package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class TypeParamModel<T extends @Nullable Object>
{
  @Memoize
  public @Nullable T count( final @Nullable String param )
  {
    return null;
  }
}
