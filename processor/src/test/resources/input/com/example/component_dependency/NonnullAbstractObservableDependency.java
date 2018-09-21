package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

@ArezComponent
abstract class NonnullAbstractObservableDependency
{
  @Observable
  @ComponentDependency
  @Nonnull
  abstract DisposeTrackable getValue();

  abstract void setValue( @Nonnull DisposeTrackable value );
}
