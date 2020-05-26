package com.example.component_dependency;

import arez.annotations.ActAsComponent;
import arez.annotations.ComponentDependency;

@ActAsComponent
abstract class BaseParameterizedFieldDependencyInParentModel<T>
{
  @ComponentDependency
  final T value = null;
}
