package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObserveParametersModel
{
  @Observe
  void doStuff( int i )
  {
  }
}
