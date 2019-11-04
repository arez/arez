package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class MissingObservableSetterModel
{
  private long _field;

  @Observable
  public long getField()
  {
    return _field;
  }
}
