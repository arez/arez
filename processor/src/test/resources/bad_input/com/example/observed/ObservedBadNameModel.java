package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class ObservedBadNameModel
{
  @Observed( name = "-ace" )
  void foo()
  {
  }
}