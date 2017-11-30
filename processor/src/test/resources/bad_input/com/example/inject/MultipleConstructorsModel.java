package com.example.inject;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@ArezComponent( inject = true )
public class MultipleConstructorsModel
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
