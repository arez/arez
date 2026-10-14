package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class BadObservableName2Model
{
  private long _field;

  @Observable( name = "default" )
  public long getField()
  {
    return _field;
  }

  public void setField( final long field )
  {
    _field = field;
  }
}
