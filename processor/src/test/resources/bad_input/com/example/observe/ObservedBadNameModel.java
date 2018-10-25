package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent
public abstract class ObservedBadNameModel
{
  @Observe( name = "-ace" )
  void foo()
  {
  }
}
