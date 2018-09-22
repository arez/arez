package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class NonnullFieldDependencyModel
{
  @Nonnull
  @ComponentDependency( action = ComponentDependency.Action.CASCADE )
  public final DisposeTrackable time = new DisposeTrackable()
  {
    @Nonnull
    @Override
    public DisposeNotifier getNotifier()
    {
      return null;
    }
  };
}
