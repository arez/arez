package com.example.dagger;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( dagger = Feature.ENABLE, sting = Feature.DISABLE )
public abstract class MultipleConstructorsModel
{
  MultipleConstructorsModel()
  {
  }

  MultipleConstructorsModel( int i )
  {
  }

  @Action
  void myAction()
  {
  }
}
