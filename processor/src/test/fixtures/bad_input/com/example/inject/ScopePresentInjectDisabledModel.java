package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import javax.inject.Singleton;

@Singleton
@ArezComponent( inject = InjectMode.NONE, dagger = Feature.DISABLE )
public abstract class ScopePresentInjectDisabledModel
{
  @Action
  void myAction()
  {
  }
}
