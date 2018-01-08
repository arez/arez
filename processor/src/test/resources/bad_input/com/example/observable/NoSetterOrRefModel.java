package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

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
