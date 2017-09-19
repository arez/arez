package com.example.autorun;

import org.realityforge.arez.annotations.Autorun;
import org.realityforge.arez.annotations.Container;

@Container
public class ReadWriteAutorunModel
{
  @Autorun( mutation = true )
  public void doStuff()
  {
  }
}
