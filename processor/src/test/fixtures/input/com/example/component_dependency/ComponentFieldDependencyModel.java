package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent
abstract class ComponentFieldDependencyModel
{
  @ArezComponent( allowEmpty = true )
  abstract static class Foo
  {
  }

  @ComponentDependency
  final Foo foo = null;
}
