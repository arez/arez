package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObserveBadNameModel2
{
  @Observe( name = "float" )
  void foo()
  {
  }
}
