package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

@ArezComponent
abstract class NonnullAbstractObservableDependency
{
  @Observable
  @Dependency
  @Nonnull
  abstract DisposeTrackable getValue();

  abstract void setValue( @Nonnull DisposeTrackable value );
}
