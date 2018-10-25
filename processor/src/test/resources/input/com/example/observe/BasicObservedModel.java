package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class BasicObservedModel
{
  @Observe
  protected void doStuff()
  {
  }
}
