package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;

@ArezComponent
public abstract class ObservableInitialInitializerEnabledModel
{
  @Observable( initializer = Feature.ENABLE )
  abstract String getName();

  abstract void setName( final String name );

  @ObservableInitial
  static final String INITIAL_Name = "Bob";
}
