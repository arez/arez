package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;
import javax.annotation.Nonnull;

@ArezComponent
abstract class NonnullAbstractObservableDependency
{
  @Observable
  @ComponentDependency
  @Nonnull
  abstract DisposeNotifier getValue();

  abstract void setValue( @Nonnull DisposeNotifier value );
}
