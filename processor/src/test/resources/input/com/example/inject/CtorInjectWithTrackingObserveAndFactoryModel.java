package com.example.inject;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.InjectMode;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.annotations.PerInstance;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class CtorInjectWithTrackingObserveAndFactoryModel
{
  CtorInjectWithTrackingObserveAndFactoryModel( @Nonnull final Runnable action, @PerInstance final int count )
  {
  }

  @Observe( executor = Executor.EXTERNAL )
  void autorun()
  {
  }

  @OnDepsChange
  void onAutorunDepsChange()
  {
  }
}
