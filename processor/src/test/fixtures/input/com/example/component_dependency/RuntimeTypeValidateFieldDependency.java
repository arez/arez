package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class RuntimeTypeValidateFieldDependency
{
  @ComponentDependency( validateTypeAtRuntime = true )
  final @Nullable Object time = null;
}
