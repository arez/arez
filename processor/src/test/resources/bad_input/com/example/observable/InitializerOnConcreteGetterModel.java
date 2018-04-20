package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent
public abstract class InitializerOnConcreteGetterModel
{
  private long _field;

  @Observable( initializer = Feature.ENABLE )
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
