package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeTrackable;

@ArezComponent( allowEmpty = true )
public abstract class SetNullBasicDependency
{
  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  final DisposeTrackable getTime()
  {
    return null;
  }
}
