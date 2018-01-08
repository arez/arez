package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Observable;

@ArezComponent
public class ObservableAndComputedMethodModel
{
  private long _field;

  @Computed
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
