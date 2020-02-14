package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PostConstruct;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.PROVIDE )
public abstract class CtorInjectWithPostConstructModel
{
  CtorInjectWithPostConstructModel( @Nonnull final Runnable action )
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
