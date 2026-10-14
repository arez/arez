package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Memoize;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class MemoizeDependency
{
  @Memoize
  @ComponentDependency
  public @Nullable DisposeNotifier getValue()
  {
    return null;
  }
}
