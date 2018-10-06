package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedBadNameModel2
{
  @Observe( name = "float" )
  void foo()
  {
  }
}
