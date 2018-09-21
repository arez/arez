package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeTrackable;

@ArezComponent( allowEmpty = true )
public abstract class NonFinalDependency
{
  @ComponentDependency
  DisposeTrackable getTime()
  {
    return null;
  }
}
