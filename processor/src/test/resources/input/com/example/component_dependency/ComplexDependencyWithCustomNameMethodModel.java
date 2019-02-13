package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.ComponentNameRef;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ComplexDependencyWithCustomNameMethodModel
{
  @Nonnull
  @ComponentNameRef
  abstract String getComponentName();

  @ComponentDependency
  final DisposeNotifier getValue1()
  {
    return null;
  }

  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  DisposeNotifier getValue3()
  {
    return null;
  }

  @Observable
  void setValue3( DisposeNotifier value )
  {
  }
}
