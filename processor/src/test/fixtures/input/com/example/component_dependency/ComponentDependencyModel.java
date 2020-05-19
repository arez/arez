package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent
abstract class ComponentDependencyModel
{
  @ArezComponent( allowEmpty = true )
  abstract static class Foo
  {
  }

  @ComponentDependency
  Foo getFoo()
  {
    return null;
  }
}
