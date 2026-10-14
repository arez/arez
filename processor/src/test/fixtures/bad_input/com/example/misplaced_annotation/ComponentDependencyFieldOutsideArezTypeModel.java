package com.example.misplaced_annotation;

import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ComponentDependencyFieldOutsideArezTypeModel
{
  @ComponentDependency
  final @Nullable Object _dependency = null;
}
