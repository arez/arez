package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;

@ArezComponent
abstract class PublicAccessFieldDependencyModel
{
  @ComponentDependency
  public final DisposeNotifier time = null;
}
