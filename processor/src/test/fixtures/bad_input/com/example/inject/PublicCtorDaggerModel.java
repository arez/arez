package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import javax.inject.Singleton;

@Singleton
@ArezComponent
public abstract class PublicCtorDaggerModel
{
  public PublicCtorDaggerModel( int i )
  {

  }

  @Action
  void myAction()
  {
  }
}
