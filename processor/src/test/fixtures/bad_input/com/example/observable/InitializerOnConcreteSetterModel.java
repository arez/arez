package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent
public abstract class InitializerOnConcreteSetterModel
{
  private long _field;

  @Observable
  public long getField()
  {
    return _field;
  }

  @Observable( initializer = Feature.ENABLE )
  public void setField( final long field )
  {
    _field = field;
  }
}
