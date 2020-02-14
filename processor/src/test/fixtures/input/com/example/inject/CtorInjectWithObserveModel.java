package com.example.inject;

import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.Observe;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.PROVIDE )
public abstract class CtorInjectWithObserveModel
{
  CtorInjectWithObserveModel( @Nonnull final Runnable action )
  {
  }

  @Observe
  void autorun()
  {
  }
}
