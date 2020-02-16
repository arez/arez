package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( dagger = Feature.ENABLE )
public abstract class BasicDaggerModel
{
  @Action
  public void myActionStuff()
  {
  }
}
