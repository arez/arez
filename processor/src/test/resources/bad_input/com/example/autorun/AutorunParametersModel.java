package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class AutorunParametersModel
{
  @Autorun
  void doStuff( int i )
  {
  }
}
