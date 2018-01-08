package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public class ReadWriteAutorunModel
{
  @Autorun( mutation = true )
  public void doStuff()
  {
  }
}
