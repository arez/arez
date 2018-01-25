package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( inject = Feature.ENABLE )
public abstract class BasicInjectModel
{
  public BasicInjectModel()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
