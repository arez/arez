package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ObservableInitialFieldNoDerivedNameModel
{
  @Observable
  abstract @Nullable String getName();

  abstract void setName( final @Nullable String name );

  @ObservableInitial
  static final @Nullable String NAME = "Bob";
}
