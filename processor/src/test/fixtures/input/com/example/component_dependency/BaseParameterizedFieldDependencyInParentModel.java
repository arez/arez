package com.example.component_dependency;

import arez.annotations.ArezComponentLike;
import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponentLike
abstract class BaseParameterizedFieldDependencyInParentModel<T extends @Nullable Object>
{
  @ComponentDependency
  final @Nullable T value = null;
}
