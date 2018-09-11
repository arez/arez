package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observed;

@ArezComponent
public abstract class ObservedDuplicateModel2
{
  @Observed( name = "doStuff" )
  void foo()
  {
  }

  @Computed
  void doStuff()
  {
  }
}
