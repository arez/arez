package com.example.misplaced_annotation;

import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ComponentDependencyMethodOutsideArezTypeModel
{
  @ComponentDependency
  @Nullable Object getDependency()
  {
    return null;
  }
}
