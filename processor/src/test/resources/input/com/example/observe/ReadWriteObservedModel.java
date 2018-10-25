package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ReadWriteObservedModel
{
  @Observe( mutation = true )
  protected void doStuff()
  {
  }
}
