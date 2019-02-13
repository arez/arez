package com.example.component_dependency;

import arez.SafeProcedure;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class NonnullFieldDependencyModel
{
  @Nonnull
  @ComponentDependency( action = ComponentDependency.Action.CASCADE )
  public final DisposeTrackable time = new DisposeTrackable()
  {
    @Override
    public void addOnDisposeListener( @Nonnull final Object key, @Nonnull final SafeProcedure action )
    {
    }

    @Override
    public void removeOnDisposeListener( @Nonnull final Object key )
    {
    }
  };
}
