package com.example.computed;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class DuplicateComputedModel
{
  @Computed( name = "ace" )
  public int getX()
  {
    return 0;
  }

  @Computed( name = "ace" )
  public int getX2()
  {
    return 0;
  }
}
