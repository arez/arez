package com.example.sting;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import java.util.concurrent.Callable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( sting = Feature.ENABLE )
public abstract class InjectArrayTypeStingModel
{
  InjectArrayTypeStingModel( @NonNull final Callable<int[]> action )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
