package com.example.component_dependency;

import arez.Disposable;
import arez.SafeProcedure;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class CascadeDisposeAndFieldDependency
{
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
    public void addOnDisposeListener( @Nonnull final Object key, @Nonnull final SafeProcedure action )
    {
    }

    @Override
    public void removeOnDisposeListener( @Nonnull final Object key )
    {
    }
  }

  @CascadeDispose
  @ComponentDependency
  final Element time = null;
}
