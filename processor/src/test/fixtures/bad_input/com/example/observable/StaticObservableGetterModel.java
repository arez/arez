package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class StaticObservableGetterModel
{
  @Observable
  public static long getField()
  {
    return 1;
  }

  @Observable
  public void setField( final long field )
  {
  }
}
