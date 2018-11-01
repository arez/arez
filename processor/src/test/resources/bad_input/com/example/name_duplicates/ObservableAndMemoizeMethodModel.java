package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Observable;

@ArezComponent
public abstract class ObservableAndMemoizeMethodModel
{
  private long _field;

  @Memoize
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
