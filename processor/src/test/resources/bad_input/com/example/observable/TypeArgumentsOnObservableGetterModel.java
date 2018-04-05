package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class TypeArgumentsOnObservableGetterModel
{
  @Observable( expectSetter = false )
  public <T> T getField()
  {
    return null;
  }

  @Nonnull
  @ObservableRef
  public abstract arez.Observable getFieldObservable();
}
