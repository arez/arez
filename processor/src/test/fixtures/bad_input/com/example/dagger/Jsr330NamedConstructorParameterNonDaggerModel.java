package com.example.dagger;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import javax.inject.Named;

@ArezComponent( dagger = Feature.DISABLE, sting = Feature.DISABLE )
public abstract class Jsr330NamedConstructorParameterNonDaggerModel
{
  Jsr330NamedConstructorParameterNonDaggerModel( @Named final String param )
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
