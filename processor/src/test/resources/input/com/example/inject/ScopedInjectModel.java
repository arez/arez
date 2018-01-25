package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import javax.inject.Singleton;

@Singleton
@ArezComponent
public abstract class ScopedInjectModel
{
  public ScopedInjectModel()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
