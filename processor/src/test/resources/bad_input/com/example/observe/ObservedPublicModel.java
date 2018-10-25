package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedPublicModel
{
  @Observe
  public void doStuff()
  {
  }
}
