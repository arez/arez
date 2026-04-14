package com.example.component_dependency;

import arez.annotations.ActAsComponent;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent
abstract class ActAsComponentFieldDependencyModel
{
  @ComponentDependency
  final MyType time = null;

  @ActAsComponent
  public interface MyType
  {
  }
}
