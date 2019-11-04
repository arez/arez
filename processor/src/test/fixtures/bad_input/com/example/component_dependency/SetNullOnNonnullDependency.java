package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class SetNullOnNonnullDependency
{
  @Nonnull
  @Observable
  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  abstract DisposeNotifier getValue();

  abstract void setValue( @Nonnull DisposeNotifier value );
}
