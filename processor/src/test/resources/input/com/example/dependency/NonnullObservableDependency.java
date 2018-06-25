package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

@ArezComponent
abstract class NonnullObservableDependency
{
  @Observable
  @Dependency
  @Nonnull
  DisposeTrackable getValue()
  {
    return null;
  }

  void setValue( @Nonnull DisposeTrackable value )
  {
  }
}
