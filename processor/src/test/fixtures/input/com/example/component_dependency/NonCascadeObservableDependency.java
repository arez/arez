package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;

@ArezComponent
abstract class NonCascadeObservableDependency
{
  @Observable( initializer = Feature.ENABLE )
  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  abstract DisposeNotifier getValue();

  abstract void setValue( DisposeNotifier value );
}
