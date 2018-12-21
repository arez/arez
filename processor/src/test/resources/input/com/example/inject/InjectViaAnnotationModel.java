package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import javax.inject.Inject;

@ArezComponent
public abstract class InjectViaAnnotationModel
{
  @Inject
  Runnable _action;

  InjectViaAnnotationModel()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
