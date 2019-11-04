package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.PerInstance;
import javax.inject.Singleton;

@Singleton
@ArezComponent
public abstract class PerInstanceParamOnProvideModel
{
  PerInstanceParamOnProvideModel( @PerInstance int i )
  {

  }

  @Action
  void myAction()
  {
  }
}
