package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ObserveParametersModel
{
  @Observe
  void doStuff( int i )
  {
  }
}
