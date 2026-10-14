package com.example.component_dependency;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class SetNullObservableNoSetterDependency
{
  @Observable( expectSetter = false )
  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  @Nullable DisposeNotifier getTime()
  {
    return null;
  }

  @ObservableValueRef
  abstract @Nullable ObservableValue<DisposeNotifier> getTimeObservableValue();
}
