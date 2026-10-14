package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ObservableInitialNonAbstractObservableModel
{
  private @Nullable String _name;

  @Observable
  @Nullable String getName()
  {
    return _name;
  }

  @Observable
  void setName( final @Nullable String name )
  {
    _name = name;
  }

  @ObservableInitial
  static final @Nullable String INITIAL_Name = "Bob";
}
