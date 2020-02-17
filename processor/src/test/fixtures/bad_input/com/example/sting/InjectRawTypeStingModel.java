package com.example.sting;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;

@ArezComponent( sting = Feature.ENABLE, dagger = Feature.DISABLE )
public abstract class InjectRawTypeStingModel
{
  @SuppressWarnings( "rawtypes" )
  InjectRawTypeStingModel( @Nonnull final Callable action )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
