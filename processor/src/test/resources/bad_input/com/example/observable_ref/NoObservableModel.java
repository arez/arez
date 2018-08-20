package com.example.observable_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ObservableRef;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class NoObservableModel
{
  @Nonnull
  @ObservableRef
  public abstract ObservableValue getTimeObservable();
}
