package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;
import javax.annotation.Nonnull;

@ArezComponent
abstract class NonnullObservableDependency
{
  @SuppressWarnings( "ConstantConditions" )
  @Observable
  @ComponentDependency
  @Nonnull
  DisposeNotifier getValue()
  {
    return null;
  }

  void setValue( @Nonnull DisposeNotifier value )
  {
  }
}
