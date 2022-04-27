package com.example.sting;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( sting = Feature.ENABLE )
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
