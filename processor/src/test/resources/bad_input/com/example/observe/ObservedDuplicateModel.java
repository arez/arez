package com.example.observe;

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
