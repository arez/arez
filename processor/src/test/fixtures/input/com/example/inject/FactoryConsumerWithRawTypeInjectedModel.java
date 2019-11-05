package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import java.util.concurrent.Callable;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class FactoryConsumerWithRawTypeInjectedModel
{
  FactoryConsumerWithRawTypeInjectedModel( @PerInstance final int count, @SuppressWarnings( "rawtypes" ) final Callable action )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
