package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class CtorInjectWithFactoryModel
{
  CtorInjectWithFactoryModel( @Nonnull final Runnable action, @PerInstance final int count )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
