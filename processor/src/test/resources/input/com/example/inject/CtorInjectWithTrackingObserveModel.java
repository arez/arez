package com.example.inject;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.InjectMode;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class CtorInjectWithTrackingObserveModel
{
  CtorInjectWithTrackingObserveModel( @Nonnull final Runnable action )
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
