package com.example.name_duplicates;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

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
