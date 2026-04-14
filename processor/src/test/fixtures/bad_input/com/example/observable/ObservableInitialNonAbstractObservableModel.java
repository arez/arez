package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;

@ArezComponent
public abstract class ObservableInitialNonAbstractObservableModel
{
  private String _name;

  @Observable
  String getName()
  {
    return _name;
  }

  @Observable
  void setName( final String name )
  {
    _name = name;
  }

  @ObservableInitial
  static final String INITIAL_Name = "Bob";
}
