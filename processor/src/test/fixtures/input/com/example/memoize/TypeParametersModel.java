package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class TypeParametersModel
{
  @Memoize
  public <T extends Integer> @Nullable T getTime()
  {
    return null;
  }
}
