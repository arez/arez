package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ObservableInitialMissingNonnullFieldModel
{
  @Observable
  @NonNull
  abstract String getName();

  abstract void setName( @NonNull String name );

  @ObservableInitial
  static final @Nullable String INITIAL_NAME = "Bob";
}
