package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import java.util.concurrent.Callable;
import javax.inject.Singleton;

@SuppressWarnings( "rawtypes" )
@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class FactoryConsumerWithRawTypeInjectedSuppressedAtClassModel
{
  FactoryConsumerWithRawTypeInjectedSuppressedAtClassModel( @PerInstance final int count, final Callable action )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
