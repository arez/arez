package com.example.dagger;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;

@ArezComponent( dagger = Feature.ENABLE, sting = Feature.DISABLE )
public abstract class InjectParameterizedTypeDaggerModel
{
  InjectParameterizedTypeDaggerModel( @Nonnull final Callable<String> action )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
