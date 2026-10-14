package com.example.sting;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( sting = Feature.ENABLE )
public abstract class BasicStingModel
{
  @Action
  public void myActionStuff()
  {
  }
}
