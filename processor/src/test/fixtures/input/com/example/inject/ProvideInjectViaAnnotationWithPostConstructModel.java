package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.PROVIDE )
public abstract class ProvideInjectViaAnnotationWithPostConstructModel
{
  @Inject
  Runnable _action;

  ProvideInjectViaAnnotationWithPostConstructModel()
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
