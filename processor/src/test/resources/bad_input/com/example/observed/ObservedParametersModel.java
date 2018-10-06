package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedParametersModel
{
  @Observe
  void doStuff( int i )
  {
  }
}
