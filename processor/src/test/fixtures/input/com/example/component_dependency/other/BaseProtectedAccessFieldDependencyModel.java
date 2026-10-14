package com.example.component_dependency.other;

import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseProtectedAccessFieldDependencyModel
{
  @ComponentDependency
  protected final @Nullable DisposeNotifier time = null;
}
