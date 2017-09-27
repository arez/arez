package com.example.observable;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class StaticObservableGetterModel
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
