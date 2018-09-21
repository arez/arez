package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class ObservableDependency
{
  @Observable
  @ComponentDependency
  DisposeTrackable getValue()
  {
    return null;
  }

  void setValue( DisposeTrackable value )
  {
  }
}
