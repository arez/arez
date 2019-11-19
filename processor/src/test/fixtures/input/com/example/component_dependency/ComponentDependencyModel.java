package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent
abstract class ComponentDependencyModel
{
  @ArezComponent( allowEmpty = true )
  static abstract class Foo
  {
  }

  @ComponentDependency
  final Foo getFoo()
  {
    return null;
  }
}
