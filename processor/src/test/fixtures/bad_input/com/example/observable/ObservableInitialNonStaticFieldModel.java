package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;

@ArezComponent
public abstract class ObservableInitialNonStaticFieldModel
{
  @Observable
  abstract String getName();

  abstract void setName( final String name );

  @ObservableInitial
  final String INITIAL_Name = "Bob";
}
