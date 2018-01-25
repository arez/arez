package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.PostDispose;

@ArezComponent
public abstract class ObservableAndPostDisposeMethodModel
{
  private long _field;

  @Action
  public long getField()
  {
    return _field;
  }

  @Observable
  @PostDispose
  public void setField( final long field )
  {
    _field = field;
  }
}
