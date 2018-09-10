package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class ReadWriteAutorunModel
{
  @Observed( mutation = true )
  protected void doStuff()
  {
  }
}
