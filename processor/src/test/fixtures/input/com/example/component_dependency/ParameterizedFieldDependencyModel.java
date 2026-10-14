package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ParameterizedFieldDependencyModel<T extends @Nullable Object>
{
  @ComponentDependency( validateTypeAtRuntime = true )
  final @Nullable T value = null;
}
