package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ArezComponent
abstract class AnnotatedModel
{
  @Memoize
  @Nonnull
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
