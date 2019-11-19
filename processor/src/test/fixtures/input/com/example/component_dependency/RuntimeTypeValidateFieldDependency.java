package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent
abstract class RuntimeTypeValidateFieldDependency
{
  @ComponentDependency( validateTypeAtRuntime = true )
  final Object time = null;
}
