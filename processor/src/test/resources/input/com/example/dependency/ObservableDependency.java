package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class ObservableDependency
{
  @Observable
  @Dependency
  DisposeTrackable getValue()
  {
    return null;
  }

  void setValue( DisposeTrackable value )
  {
  }
}
