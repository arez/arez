package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class DeprecatedAutorunModel
{
  @Deprecated
  @Autorun
  protected void doStuff()
  {
  }
}
