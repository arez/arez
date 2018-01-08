package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public class ExtraParameterGetterModel
{
  private long _field;

  @Observable
  public long getField( final int ignored )
  {
    return _field;
  }

  @Observable
  public void setField( final long field )
  {
    _field = field;
  }
}
