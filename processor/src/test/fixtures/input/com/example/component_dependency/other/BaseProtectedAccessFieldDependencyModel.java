package com.example.component_dependency.other;

import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;

public abstract class BaseProtectedAccessFieldDependencyModel
{
  @ComponentDependency
  protected final DisposeNotifier time = null;
}
