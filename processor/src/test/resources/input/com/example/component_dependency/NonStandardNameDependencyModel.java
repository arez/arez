package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class NonStandardNameDependencyModel
{
  @ComponentDependency
  public final DisposeTrackable $$MYDEP$$()
  {
    return null;
  }
}
