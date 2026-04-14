package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;

@ArezComponent
abstract class ProtectedAccessFieldDependencyModel
{
  @ComponentDependency
  protected final DisposeNotifier time = null;
}
