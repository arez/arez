package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class NonAbstractSetterModel
{
  private long _field;

  @Observable
  public abstract long getField();

  @Observable
  public void setField( final long field )
  {
    _field = field;
  }
}
