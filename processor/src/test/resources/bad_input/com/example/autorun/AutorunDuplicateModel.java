package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public class AutorunDuplicateModel
{
  @Autorun( name = "doStuff" )
  void foo()
  {
  }

  @Autorun
  void doStuff()
  {
  }
}
