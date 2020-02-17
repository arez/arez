package com.example.dagger;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import javax.inject.Singleton;

@Singleton
@ArezComponent( dagger = Feature.DISABLE, sting = Feature.DISABLE )
public abstract class Jsr330ScopedNonDaggerModel
{
  @Action
  void myAction()
  {
  }
}
