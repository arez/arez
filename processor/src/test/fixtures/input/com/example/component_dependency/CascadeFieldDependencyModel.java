package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;

@ArezComponent
abstract class CascadeFieldDependencyModel
{
  @ComponentDependency( action = ComponentDependency.Action.CASCADE )
  public final DisposeNotifier time = null;
}
