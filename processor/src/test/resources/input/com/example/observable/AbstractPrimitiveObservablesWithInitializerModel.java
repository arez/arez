package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent
public abstract class AbstractPrimitiveObservablesWithInitializerModel
{
  @Observable( initializer = Feature.ENABLE )
  public abstract long getTime();

  public abstract void setTime( long value );
}
