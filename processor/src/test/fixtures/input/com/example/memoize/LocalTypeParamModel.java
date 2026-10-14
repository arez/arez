package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class LocalTypeParamModel
{
  // This fixture deliberately tests a method-local return type parameter.
  @SuppressWarnings("TypeParameterUnusedInFormals")
  @Memoize
  public <T extends @Nullable Object> @Nullable T count( final @Nullable String param )
  {
    return null;
  }
}
