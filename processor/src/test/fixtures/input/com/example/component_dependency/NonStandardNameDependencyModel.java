package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;

@ArezComponent
abstract class NonStandardNameDependencyModel
{
  @ComponentDependency
  public DisposeNotifier $$MYDEP$$()
  {
    return null;
  }
}
