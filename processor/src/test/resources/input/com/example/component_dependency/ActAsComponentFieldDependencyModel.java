package com.example.component_dependency;

import arez.annotations.ActAsComponent;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;

@ArezComponent
public abstract class ActAsComponentFieldDependencyModel
{
  @ComponentDependency
  public final MyType time = null;

  @ActAsComponent
  public interface MyType
  {
  }
}
