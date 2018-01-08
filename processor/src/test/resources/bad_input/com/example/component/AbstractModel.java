package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class AbstractModel
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
