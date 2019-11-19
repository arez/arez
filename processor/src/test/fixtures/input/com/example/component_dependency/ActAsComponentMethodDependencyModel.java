package com.example.component_dependency;

import arez.annotations.ActAsComponent;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent
abstract class ActAsComponentMethodDependencyModel
{
  @ComponentDependency
  public final MyType getTime()
  {
    return null;
  }

  @ActAsComponent
  public interface MyType
  {
  }
}
