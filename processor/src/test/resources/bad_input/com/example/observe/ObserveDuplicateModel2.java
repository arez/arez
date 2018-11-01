package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObserveDuplicateModel2
{
  @Observe( name = "doStuff" )
  void foo()
  {
  }

  @Memoize
  void doStuff()
  {
  }
}
