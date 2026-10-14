package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class AnnotatedModel
{
  @Memoize
  @NonNull
  public String getTime()
  {
    return "";
  }

  @Memoize
  @Nullable
  public String count( final long time, float someOtherParameter )
  {
    return null;
  }
}
