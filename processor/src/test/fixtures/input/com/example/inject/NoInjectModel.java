package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( sting = Feature.DISABLE )
public abstract class NoInjectModel
{
  @Action
  public void myActionStuff()
  {
  }
}
