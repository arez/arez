package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import javax.inject.Inject;

@ArezComponent
public abstract class Jsr330InjectFieldDaggerModel
{
  @Inject
  Runnable _myService;

  @Action
  void myAction()
  {
  }
}
