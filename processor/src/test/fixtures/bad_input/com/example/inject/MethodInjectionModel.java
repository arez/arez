package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import javax.inject.Inject;

@ArezComponent
public abstract class MethodInjectionModel
{
  @Inject
  void setService( Runnable myService )
  {
  }

  @Action
  void myAction()
  {
  }
}
