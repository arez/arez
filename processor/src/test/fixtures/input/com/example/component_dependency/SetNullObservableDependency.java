package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class SetNullObservableDependency
{
  @Observable
  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  abstract @Nullable DisposeNotifier getValue();

  abstract void setValue( @Nullable DisposeNotifier value );
}
