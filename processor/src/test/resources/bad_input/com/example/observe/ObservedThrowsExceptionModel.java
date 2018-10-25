package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import java.text.ParseException;

@ArezComponent
public abstract class ObservedThrowsExceptionModel
{
  @Observe
  void doStuff()
    throws ParseException
  {
  }
}
