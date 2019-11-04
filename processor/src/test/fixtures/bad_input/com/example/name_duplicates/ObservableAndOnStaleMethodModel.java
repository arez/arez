package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.OnStale;

@ArezComponent
public abstract class ObservableAndOnStaleMethodModel
{
  private long _field;

  @Action
  public long getField()
  {
    return _field;
  }

  @Observable
  @OnStale
  public void setField( final long field )
  {
    _field = field;
  }
}
