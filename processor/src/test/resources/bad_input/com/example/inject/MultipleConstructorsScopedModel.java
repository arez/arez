package com.example.inject;

import javax.inject.Singleton;
import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

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
