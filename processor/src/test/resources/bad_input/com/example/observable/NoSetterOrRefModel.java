package com.example.observable;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class NoSetterOrRefModel
{
  private long _field;

  @Observable( expectSetter = false )
  public long getField()
  {
    return _field;
  }
}
