package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.Observe;
import arez.annotations.PostConstruct;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
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
