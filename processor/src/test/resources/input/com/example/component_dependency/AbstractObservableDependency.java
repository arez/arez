package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class AbstractObservableDependency
{
  @Observable
  @ComponentDependency
  abstract DisposeTrackable getValue();

  abstract void setValue( DisposeTrackable value );
}
