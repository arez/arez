package com.example.inject;

import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.Observe;
import arez.annotations.PerInstance;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class CtorInjectWithObserveAndFactoryModel
{
  CtorInjectWithObserveAndFactoryModel( @Nonnull final Runnable action, @PerInstance final int count )
  {
  }

  @Observe
  void autorun()
  {
  }
}
