package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Memoize;
import arez.component.DisposeNotifier;

@ArezComponent
public abstract class MemoizeDependency
{
  @Memoize
  @ComponentDependency
  public DisposeNotifier getValue()
  {
    return null;
  }
}
