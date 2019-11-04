package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import javax.inject.Inject;

@ArezComponent( inject = InjectMode.NONE )
public abstract class InjectMethodsInjectDisabledModel
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
