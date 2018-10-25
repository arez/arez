package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedPrivateModel
{
  @Observe
  private void doStuff()
  {
  }
}
