package com.example.to_string;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ToStringPresent
{
  @Override
  public @Nullable String toString()
  {
    return super.toString();
  }
}
