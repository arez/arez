package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class SetNullOnNonnullDependency
{
  @Nonnull
  @Observable
  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  abstract DisposeTrackable getValue();

  abstract void setValue( @Nonnull DisposeTrackable value );
}
