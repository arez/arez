package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedDuplicateModel
{
  @Observe( name = "doStuff" )
  void foo()
  {
  }

  @Observe
  void doStuff()
  {
  }
}
