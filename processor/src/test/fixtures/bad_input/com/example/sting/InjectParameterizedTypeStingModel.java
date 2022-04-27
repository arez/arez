package com.example.sting;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;

@ArezComponent( sting = Feature.ENABLE )
public abstract class InjectParameterizedTypeStingModel
{
  InjectParameterizedTypeStingModel( @Nonnull final Callable<String> action )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
