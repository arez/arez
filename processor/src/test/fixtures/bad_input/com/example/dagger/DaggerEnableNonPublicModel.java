package com.example.dagger;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( dagger = Feature.ENABLE, sting = Feature.DISABLE )
abstract class DaggerEnableNonPublicModel
{
  @Action
  void myAction()
  {
  }
}
