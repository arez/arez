package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@ArezComponent
public abstract class Jsr330ScopedModel
{
  @Action
  public void myActionStuff()
  {
  }
}
