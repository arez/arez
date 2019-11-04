package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class SetterWithExpectSetterFalseModel
{
  private long _field;

  @Observable
  public long getField()
  {
    return _field;
  }

  @Observable( expectSetter = false )
  public void setField( final long field )
  {
    _field = field;
  }
}
