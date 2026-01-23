package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;

@ArezComponent
public abstract class ObservableInitialNonFinalFieldModel
{
  @Observable
  abstract String getName();

  abstract void setName( final String name );

  @ObservableInitial
  static String INITIAL_Name = "Bob";
}
