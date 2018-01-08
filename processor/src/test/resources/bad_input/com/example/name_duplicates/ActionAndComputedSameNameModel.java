package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public class ActionAndComputedSameNameModel
{
  @Computed( name = "x" )
  public long m1()
  {
    return 22;
  }

  @Action( name = "x" )
  public long m2()
  {
    return 22;
  }
}
