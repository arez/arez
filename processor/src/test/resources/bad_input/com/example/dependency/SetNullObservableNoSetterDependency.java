package com.example.dependency;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.component.DisposeTrackable;

@ArezComponent( allowEmpty = true )
public abstract class SetNullObservableNoSetterDependency
{
  @Observable( expectSetter = false )
  @Dependency( action = Dependency.Action.SET_NULL )
  DisposeTrackable getTime()
  {
    return null;
  }

  @ObservableValueRef
  abstract ObservableValue<DisposeTrackable> getTimeObservableValue();
}
