package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;

@ArezComponent
public abstract class ObservableInitialDuplicateModel
{
  @Observable
  abstract String getName();

  abstract void setName( final String name );

  @ObservableInitial
  static final String INITIAL_Name = "Bob";

  @ObservableInitial
  static String getInitialName()
  {
    return "Jim";
  }
}
