package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservePrivateModel
{
  @Observe
  private void doStuff()
  {
  }
}
