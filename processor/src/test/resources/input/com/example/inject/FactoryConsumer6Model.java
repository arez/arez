package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import arez.annotations.PostConstruct;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class FactoryConsumer6Model
{
  @Inject
  Runnable _action;

  FactoryConsumer6Model( @PerInstance final int count, @PerInstance @Nullable final String nonPerInstanceValue )
  {
  }

  @PostConstruct
  final void postConstruct()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
