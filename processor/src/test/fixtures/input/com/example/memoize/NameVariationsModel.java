package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class NameVariationsModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @Memoize
  public boolean isReady()
  {
    return true;
  }

  @Memoize
  public @Nullable String helper()
  {
    return "";
  }

  @Memoize( name = "foo" )
  public @Nullable String myFooHelperMethod()
  {
    return "";
  }
}
