package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import javax.inject.Singleton;

@Singleton
@ArezComponent( dagger = Feature.DISABLE )
public abstract class ScopedButNoDaggerModel
{
  ScopedButNoDaggerModel()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
