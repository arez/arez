package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class StaticObservableSetterModel
{
  @Observable
  public long getField()
  {
    return 1;
  }

  @Observable
  public static void setField( final long field )
  {
  }
}
