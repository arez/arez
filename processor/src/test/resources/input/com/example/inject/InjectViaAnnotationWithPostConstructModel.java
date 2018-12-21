package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ArezComponent
public abstract class InjectViaAnnotationWithPostConstructModel
{
  @Inject
  Runnable _action;

  InjectViaAnnotationWithPostConstructModel()
  {
  }

  @PostConstruct
  final void postConstruct()
  {
  }

  @Action
  public void myActionStuff()
  {
  }
}
