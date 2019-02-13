package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;

@ArezComponent
public abstract class BasicDependencyModel
{
  @ComponentDependency
  public final DisposeNotifier getTime()
  {
    return null;
  }
}
