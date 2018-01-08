package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public class BadComputedName2Model
{
  @Computed( name = "ace-" )
  public int setField()
  {
    return 0;
  }
}
