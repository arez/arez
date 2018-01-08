package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.OnActivate;

@ArezComponent
public class ObservableAndOnActivateMethodModel
{
  private long _field;

  @Action
  public long getField()
  {
    return _field;
  }

  @Observable
  @OnActivate
  public void setField( final long field )
  {
    _field = field;
  }
}
