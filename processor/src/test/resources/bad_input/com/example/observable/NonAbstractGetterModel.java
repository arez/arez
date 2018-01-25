package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class NonAbstractGetterModel
{
  private long _field;

  @Observable
  public long getField()
  {
    return _field;
  }

  @Observable
  public abstract void setField( final long field );
}
