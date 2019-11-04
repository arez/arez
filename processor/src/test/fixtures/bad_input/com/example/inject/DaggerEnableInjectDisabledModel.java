package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;

@ArezComponent( inject = InjectMode.NONE, dagger = Feature.ENABLE)
public abstract class DaggerEnableInjectDisabledModel
{
  public DaggerEnableInjectDisabledModel()
  {
  }

  @Action
  void myAction()
  {
  }
}
