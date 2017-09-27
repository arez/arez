package com.example.observable;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class StaticObservableSetterModel
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
