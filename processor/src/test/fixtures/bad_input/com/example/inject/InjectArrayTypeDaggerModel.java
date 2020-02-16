package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;

@ArezComponent( dagger = Feature.ENABLE )
public abstract class InjectArrayTypeDaggerModel
{
  InjectArrayTypeDaggerModel( @Nonnull final Callable<int[]> action )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
