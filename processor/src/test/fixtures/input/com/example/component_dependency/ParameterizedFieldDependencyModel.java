package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent
abstract class ParameterizedFieldDependencyModel<T>
{
  @ComponentDependency( validateTypeAtRuntime = true )
  final T value = null;
}

