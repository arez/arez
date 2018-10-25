package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ReadWriteObserveModel
{
  @Observe( mutation = true )
  protected void doStuff()
  {
  }
}
