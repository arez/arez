package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class NonStandardNameFieldDependencyModel
{
  @ComponentDependency
  public final DisposeTrackable $$time$$ = null;
}
