package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;

@ArezComponent
public abstract class SetNullObservableDependency
{
  @Observable
  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  abstract DisposeNotifier getValue();

  abstract void setValue( DisposeNotifier value );
}
