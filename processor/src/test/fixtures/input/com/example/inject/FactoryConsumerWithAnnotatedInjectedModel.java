package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import arez.annotations.PostConstruct;
import javax.annotation.Nullable;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class FactoryConsumerWithAnnotatedInjectedModel
{
  FactoryConsumerWithAnnotatedInjectedModel( @PerInstance final int count, @Nullable final Runnable nonPerInstanceValue )
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
