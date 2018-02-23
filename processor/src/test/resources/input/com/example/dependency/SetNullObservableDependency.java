package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;

@ArezComponent
public abstract class SetNullObservableDependency
{
  @Observable
  @Dependency( action = Dependency.Action.SET_NULL )
  abstract Object getValue();

  abstract void setValue( Object value );
}
