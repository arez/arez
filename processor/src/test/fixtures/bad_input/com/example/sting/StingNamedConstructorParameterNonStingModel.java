package com.example.sting;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Named;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( sting = Feature.DISABLE )
public abstract class StingNamedConstructorParameterNonStingModel
{
  StingNamedConstructorParameterNonStingModel( @Named( "" ) final @Nullable String param )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
