package com.example.package_access.other;

import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseComponentDependencyModel
{
  @ComponentDependency
  @Nullable Object getTime()
  {
    return null;
  }
}
