package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObserveReturnsValueModel
{
  @Observe
  int doStuff()
  {
    return 0;
  }
}
