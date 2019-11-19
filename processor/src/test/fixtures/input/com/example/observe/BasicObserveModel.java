package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
abstract class BasicObserveModel
{
  @Observe
  protected void doStuff()
  {
  }
}
