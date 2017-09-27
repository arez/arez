package com.example.autorun;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;

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
