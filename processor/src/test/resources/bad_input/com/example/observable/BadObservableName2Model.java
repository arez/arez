package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class BadObservableName2Model
{
  private long _field;

  @Observable( name = "ace-" )
  public long getField()
  {
    return _field;
  }

  @Observable( name = "ace-" )
  public void setField( final long field )
  {
    _field = field;
  }
}
