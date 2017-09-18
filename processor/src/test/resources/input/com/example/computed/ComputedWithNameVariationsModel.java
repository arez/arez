package com.example.computed;

import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
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
