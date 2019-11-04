package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class FactoryConsumer7Model
{
  FactoryConsumer7Model( @PerInstance final int count, final Runnable action )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
