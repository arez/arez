package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ReadWriteObserveModel
{
  @Observe( mutation = true )
  void doStuff()
  {
  }
}
