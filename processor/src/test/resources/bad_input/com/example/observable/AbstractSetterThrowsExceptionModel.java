package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class AbstractSetterThrowsExceptionModel
{
  @Observable
  public abstract long getField();

  public abstract void setField( long v )
    throws Exception;
}
