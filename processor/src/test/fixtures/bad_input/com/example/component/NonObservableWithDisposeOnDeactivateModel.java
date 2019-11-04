package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent( observable = Feature.DISABLE, disposeOnDeactivate = true )
public abstract class NonObservableWithDisposeOnDeactivateModel
{
  private long _field;

  @Observable
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
