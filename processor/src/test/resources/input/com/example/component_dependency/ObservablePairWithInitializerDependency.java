package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class ObservablePairWithInitializerDependency
{
  @ComponentDependency
  abstract DisposeTrackable getValue();

  @Observable( initializer = Feature.ENABLE )
  abstract void setValue( DisposeTrackable value );
}
