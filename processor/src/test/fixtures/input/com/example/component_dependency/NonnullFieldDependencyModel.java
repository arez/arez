package com.example.component_dependency;

import arez.SafeProcedure;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import javax.annotation.Nonnull;

@ArezComponent
abstract class NonnullFieldDependencyModel
{
  @Nonnull
  @ComponentDependency( action = ComponentDependency.Action.CASCADE )
  public final DisposeNotifier time = new DisposeNotifier()
  {
    @Override
    public void addOnDisposeListener( @Nonnull final Object key,
                                      @Nonnull final SafeProcedure action,
                                      final boolean errorIfDuplicate )
    {
    }

    @Override
    public void removeOnDisposeListener( @Nonnull final Object key, final boolean errorIfMissing )
    {
    }
  };
}
