package com.example.component_dependency;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class CascadeDisposeAndFieldDependency
{
  static class Element
    implements DisposeTrackable, Disposable
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

    @Nonnull
    @Override
    public DisposeNotifier getNotifier()
    {
      return null;
    }
  }

  @CascadeDispose
  @ComponentDependency
  final Element time = null;
}
