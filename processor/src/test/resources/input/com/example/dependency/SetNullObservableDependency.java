package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class SetNullObservableDependency
{
  @Observable
  @Dependency( action = Dependency.Action.SET_NULL )
  abstract DisposeTrackable getValue();

  abstract void setValue( DisposeTrackable value );
}
