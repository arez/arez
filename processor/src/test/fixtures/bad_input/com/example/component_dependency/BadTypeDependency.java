package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class BadTypeDependency
{
  @ComponentDependency
  public final @Nullable Object getValue()
  {
    return null;
  }
}
