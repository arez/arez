package com.example.observable_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ObservableRef;
import javax.annotation.Nonnull;

@ArezComponent
public class NoObservableModel
{
  @Nonnull
  @ObservableRef
  public arez.Observable getTimeObservable()
  {
    throw new IllegalStateException();
  }
}
