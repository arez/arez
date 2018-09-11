package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class ReadWriteObservedModel
{
  @Observed( mutation = true )
  protected void doStuff()
  {
  }
}
