package com.example.sting;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Named;

@ArezComponent( sting = Feature.DISABLE )
public abstract class StingNamedConstructorParameterNonStingModel
{
  StingNamedConstructorParameterNonStingModel( @Named( "" ) final String param )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
