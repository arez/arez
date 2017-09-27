package com.example.observable;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class MissingObservableSetterModel
{
  private long _field;

  @Observable
  public long getField()
  {
    return _field;
  }
}
