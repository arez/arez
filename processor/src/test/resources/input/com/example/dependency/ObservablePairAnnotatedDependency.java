package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class ObservablePairAnnotatedDependency
{
  @Dependency
  abstract DisposeTrackable getValue();

  @Observable
  abstract void setValue( DisposeTrackable value );
}
