package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public class FinalObservableSetterModel
{
  private long _field;

  @Observable
  public long getField()
  {
    return _field;
  }

  @Observable
  public final void setField( final long field )
  {
    _field = field;
  }
}
