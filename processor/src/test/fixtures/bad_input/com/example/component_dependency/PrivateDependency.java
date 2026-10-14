package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class PrivateDependency
{
  @ComponentDependency
  private @Nullable DisposeNotifier getTime()
  {
    return null;
  }
}
