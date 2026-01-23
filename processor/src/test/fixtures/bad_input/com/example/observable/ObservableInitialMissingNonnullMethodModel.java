package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ObservableInitialMissingNonnullMethodModel
{
  @Observable
  @Nonnull
  abstract String getName();

  abstract void setName( @Nonnull String name );

  @ObservableInitial
  static String getInitialName()
  {
    return "Bob";
  }
}
