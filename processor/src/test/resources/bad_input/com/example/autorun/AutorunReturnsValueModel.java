package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class AutorunReturnsValueModel
{
  @Autorun
  int doStuff()
  {
    return 0;
  }
}
