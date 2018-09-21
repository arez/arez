package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent
public abstract class BadTypeDependency
{
  @ComponentDependency
  public final Object getValue()
  {
    return null;
  }
}
