package com.example.observable;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class SetterButExpectSetterFalseModel
{
  private long _field;

  @Observable( expectSetter = false )
  public long getField()
  {
    return _field;
  }

  @Observable
  public void setField( final long field )
  {
    _field = field;
  }
}
