package com.example.computed;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class BadComputedNameModel
{
  @Computed( name = "-ace" )
  public int setField()
  {
    return 0;
  }
}
