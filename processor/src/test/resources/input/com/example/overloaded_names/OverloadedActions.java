package com.example.overloaded_names;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@ArezComponent
public class OverloadedActions
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
