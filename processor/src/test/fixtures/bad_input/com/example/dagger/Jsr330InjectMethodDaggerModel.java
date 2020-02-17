package com.example.dagger;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import javax.inject.Inject;

@ArezComponent( sting = Feature.DISABLE )
public abstract class Jsr330InjectMethodDaggerModel
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
