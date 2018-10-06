package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedDuplicateModel2
{
  @Observe( name = "doStuff" )
  void foo()
  {
  }

  @Computed
  void doStuff()
  {
  }
}
