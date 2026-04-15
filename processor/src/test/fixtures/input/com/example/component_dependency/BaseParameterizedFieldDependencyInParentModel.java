package com.example.component_dependency;

import arez.annotations.ArezComponentLike;
import arez.annotations.ComponentDependency;

@ArezComponentLike
abstract class BaseParameterizedFieldDependencyInParentModel<T>
{
  @ComponentDependency
  final T value = null;
}
