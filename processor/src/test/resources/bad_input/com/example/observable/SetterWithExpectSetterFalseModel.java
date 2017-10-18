package com.example.observable;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class SetterWithExpectSetterFalseModel
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
