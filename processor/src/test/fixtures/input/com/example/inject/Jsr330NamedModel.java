package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import javax.inject.Named;

@Named
@ArezComponent( sting = Feature.DISABLE )
public abstract class Jsr330NamedModel
{
  @Action
  public void myActionStuff()
  {
  }
}
