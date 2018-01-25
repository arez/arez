package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class ReadWriteAutorunModel
{
  @Autorun( mutation = true )
  public void doStuff()
  {
  }
}
