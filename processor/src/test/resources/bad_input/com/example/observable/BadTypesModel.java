package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public class BadTypesModel
{
  private long _field;

  @Observable
  public long getField()
  {
    return _field;
  }

  @Observable
  public void setField( final int field )
  {
    _field = field;
  }
}
