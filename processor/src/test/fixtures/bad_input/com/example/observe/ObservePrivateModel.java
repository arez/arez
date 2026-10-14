package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ObservePrivateModel
{
  @Observe
  private void doStuff()
  {
  }
}
