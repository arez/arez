package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class NameVariationsModel
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
  public String helper()
  {
    return "";
  }

  @Memoize( name = "foo" )
  public String myFooHelperMethod()
  {
    return "";
  }
}
