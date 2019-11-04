package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( dagger = Feature.ENABLE )
abstract class DaggerEnableNonPublicModel
{
  DaggerEnableNonPublicModel()
  {
  }

  @Action
  void myAction()
  {
  }
}
