package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import javax.annotation.Nonnull;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.CONSUME )
public abstract class CtorInjectModel
{
  CtorInjectModel( @Nonnull final Runnable action )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
