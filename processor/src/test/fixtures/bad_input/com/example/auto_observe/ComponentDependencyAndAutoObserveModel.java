package com.example.auto_observe;

import arez.annotations.ActAsComponent;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.ComponentDependency;

@ArezComponent
abstract class ComponentDependencyAndAutoObserveModel
{
  @ComponentDependency( validateTypeAtRuntime = true )
  @AutoObserve( validateTypeAtRuntime = true )
  final MyType _field = null;

  @ActAsComponent
  interface MyType
  {
  }
}
