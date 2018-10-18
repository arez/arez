package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class AbstractGetterThrowsExceptionModel
{
  @Observable
  public abstract long getField()
    throws Exception;

  public abstract void setField( long v );
}
