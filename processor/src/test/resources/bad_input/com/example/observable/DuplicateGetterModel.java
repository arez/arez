package com.example.observable;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class DuplicateGetterModel
{
  private long _field;

  @Observable
  public long getField()
  {
    return _field;
  }

  @Observable( name = "field" )
  public long getField2()
  {
    return _field;
  }

  @Observable
  public void setField( final long field )
  {
    _field = field;
  }
}
