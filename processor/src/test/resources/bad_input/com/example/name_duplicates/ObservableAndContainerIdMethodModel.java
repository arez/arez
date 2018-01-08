package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Observable;

@ArezComponent
public class ObservableAndContainerIdMethodModel
{
  private long _field;

  @ComponentId
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
