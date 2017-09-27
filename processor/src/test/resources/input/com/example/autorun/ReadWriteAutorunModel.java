package com.example.autorun;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;

@ArezComponent
public class ReadWriteAutorunModel
{
  @Autorun( mutation = true )
  public void doStuff()
  {
  }
}
