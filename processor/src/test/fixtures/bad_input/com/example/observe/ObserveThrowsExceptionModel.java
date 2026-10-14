package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import java.text.ParseException;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ObserveThrowsExceptionModel
{
  @SuppressWarnings( "RedundantThrows" )
  @Observe
  void doStuff()
    throws ParseException
  {
  }
}
