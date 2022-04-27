package com.example.sting;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( sting = Feature.ENABLE )
public abstract class BasicStingModel
{
  @Action
  public void myActionStuff()
  {
  }
}
