package com.example.component_dependency;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.component.DisposeTrackable;

@ArezComponent( allowEmpty = true )
public abstract class SetNullObservableNoSetterDependency
{
  @Observable( expectSetter = false )
  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  DisposeTrackable getTime()
  {
    return null;
  }

  @ObservableValueRef
  abstract ObservableValue<DisposeTrackable> getTimeObservableValue();
}
