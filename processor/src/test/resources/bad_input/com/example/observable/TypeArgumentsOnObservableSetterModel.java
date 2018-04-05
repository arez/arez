package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class TypeArgumentsOnObservableSetterModel
{
  public <T> T getField()
  {
    return null;
  }

  @Observable
  public <T> void setField( final T field )
  {
  }
}
