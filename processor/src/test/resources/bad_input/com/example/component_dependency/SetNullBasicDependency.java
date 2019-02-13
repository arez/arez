package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;

@ArezComponent( allowEmpty = true )
public abstract class SetNullBasicDependency
{
  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  final DisposeNotifier getTime()
  {
    return null;
  }
}
