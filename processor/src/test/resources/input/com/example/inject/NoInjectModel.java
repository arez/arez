package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( inject = Feature.DISABLE )
public abstract class NoInjectModel
{
  public NoInjectModel()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
