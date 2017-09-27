package com.example.computed;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class ComputedWithNameVariationsModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Computed
  public boolean isReady()
  {
    return true;
  }

  @Computed
  public String helper()
  {
    return "";
  }

  @Computed( name = "foo" )
  public String myFooHelperMethod()
  {
    return "";
  }
}
