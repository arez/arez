package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class ObservedDuplicateModel
{
  @Observed( name = "doStuff" )
  void foo()
  {
  }

  @Observed
  void doStuff()
  {
  }
}
