package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;

@ArezComponent
abstract class PrimitiveReturnAutoObserveModel
{
  @AutoObserve
  int getValue()
  {
    return 0;
  }
}
