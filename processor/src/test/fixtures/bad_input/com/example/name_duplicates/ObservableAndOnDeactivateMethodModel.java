package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.OnDeactivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ObservableAndOnDeactivateMethodModel
{
  private long _field;

  @Action
  public long getField()
  {
    return _field;
  }

  @Observable
  @OnDeactivate
  public void setField( final long field )
  {
    _field = field;
  }
}
