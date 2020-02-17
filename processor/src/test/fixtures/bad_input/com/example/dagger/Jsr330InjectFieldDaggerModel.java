package com.example.dagger;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import javax.inject.Inject;

@ArezComponent( sting = Feature.DISABLE )
public abstract class Jsr330InjectFieldDaggerModel
{
  @Inject
  Runnable _myService;

  @Action
  void myAction()
  {
  }
}
