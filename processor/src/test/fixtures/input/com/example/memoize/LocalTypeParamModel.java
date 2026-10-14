package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class LocalTypeParamModel
{
  @Memoize
  public <T extends @Nullable Object> @Nullable T count( final @Nullable String param )
  {
    return null;
  }
}
