package com.example.component_dependency;

import arez.SafeProcedure;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NonnullFieldDependencyModel
{
  @NonNull
  @ComponentDependency( action = ComponentDependency.Action.CASCADE )
  final DisposeNotifier time = new DisposeNotifier()
  {
    @Override
    public void addOnDisposeListener( @NonNull final Object key,
                                      @NonNull final SafeProcedure action,
                                      final boolean errorIfDuplicate )
    {
    }

    @Override
    public void removeOnDisposeListener( @NonNull final Object key, final boolean errorIfMissing )
    {
    }
  };
}
