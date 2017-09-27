package com.example.autorun;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;

@ArezComponent
public class AutorunReturnsValueModel
{
  @Autorun
  int doStuff()
  {
    return 0;
  }
}
