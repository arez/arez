package com.example.component_dependency;

import arez.Disposable;
import arez.SafeProcedure;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class CascadeDisposeAndFieldDependency
{
  @NullMarked
  static class Element
    implements DisposeNotifier, Disposable
  {
    @Override
    public void dispose()
    {
    }

    @Override
    public boolean isDisposed()
    {
      return false;
    }

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
  }

  @CascadeDispose
  @ComponentDependency
  final @Nullable Element time = null;
}
