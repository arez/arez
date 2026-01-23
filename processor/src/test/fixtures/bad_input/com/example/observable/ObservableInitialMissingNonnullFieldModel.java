package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ObservableInitialMissingNonnullFieldModel
{
  @Observable
  @Nonnull
  abstract String getName();

  abstract void setName( @Nonnull String name );

  @ObservableInitial
  static final String INITIAL_NAME = "Bob";
}
