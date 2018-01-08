package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import javax.inject.Singleton;

@Singleton
@ArezComponent
public class MultipleConstructorsScopedModel
{
  public MultipleConstructorsScopedModel()
  {
  }

  public MultipleConstructorsScopedModel( int i )
  {
  }

  @Action
  void myAction()
  {
  }
}
