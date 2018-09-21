package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

@ArezComponent
abstract class NonnullObservableDependency
{
  @Observable
  @ComponentDependency
  @Nonnull
  DisposeTrackable getValue()
  {
    return null;
  }

  void setValue( @Nonnull DisposeTrackable value )
  {
  }
}
