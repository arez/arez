package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;

@ArezComponent( inject = InjectMode.PROVIDE )
public abstract class MultipleConstructorsModel
{
  public MultipleConstructorsModel()
  {
  }

  public MultipleConstructorsModel( int i )
  {
  }

  @Action
  void myAction()
  {
  }
}
