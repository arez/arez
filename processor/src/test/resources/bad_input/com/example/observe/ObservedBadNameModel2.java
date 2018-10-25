package com.example.observe;

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
