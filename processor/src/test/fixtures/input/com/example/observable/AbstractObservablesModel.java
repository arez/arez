package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class AbstractObservablesModel
{
  @Observable
  public abstract long getTime();

  public abstract void setTime( long value );
}
