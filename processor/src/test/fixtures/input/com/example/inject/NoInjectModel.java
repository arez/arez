package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( sting = Feature.DISABLE )
public abstract class NoInjectModel
{
  @Action
  public void myActionStuff()
  {
  }
}
