package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.ComponentNameRef;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ComplexDependencyWithCustomNameMethodModel
{
  @Nonnull
  @ComponentNameRef
  abstract String getComponentName();

  @ComponentDependency
  final DisposeTrackable getValue1()
  {
    return null;
  }

  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  DisposeTrackable getValue3()
  {
    return null;
  }

  @Observable
  void setValue3( DisposeTrackable value )
  {
  }
}
