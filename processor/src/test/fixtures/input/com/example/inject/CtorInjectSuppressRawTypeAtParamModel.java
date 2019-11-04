package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class CtorInjectSuppressRawTypeAtParamModel
{
  CtorInjectSuppressRawTypeAtParamModel( @SuppressWarnings( "rawtypes" ) @Nonnull final Callable action )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}