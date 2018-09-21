package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class ObservablePairAnnotatedDependency
{
  @ComponentDependency
  abstract DisposeTrackable getValue();

  @Observable
  abstract void setValue( DisposeTrackable value );
}
