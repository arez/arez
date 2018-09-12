package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import java.text.ParseException;

@ArezComponent
public abstract class ObservedThrowsExceptionModel
{
  @Observed
  void doStuff()
    throws ParseException
  {
  }
}
