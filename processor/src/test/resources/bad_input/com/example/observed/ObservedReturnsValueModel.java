package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedReturnsValueModel
{
  @Observe
  int doStuff()
  {
    return 0;
  }
}
