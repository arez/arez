package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class AbstractGetterNoSetterModel
{
  @Observable( expectSetter = false )
  public abstract long getField();

  @Nonnull
  @ObservableRef
  public abstract arez.Observable getFieldObservable();
}
