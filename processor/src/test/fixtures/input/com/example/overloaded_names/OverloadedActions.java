package com.example.overloaded_names;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class OverloadedActions
{
  @Action
  public void myAction()
  {
  }

  @Action( name = "myAction2" )
  public void myAction( int i )
  {
  }

  @Action( name = "myAction3" )
  public void myAction( float i )
  {
  }
}
