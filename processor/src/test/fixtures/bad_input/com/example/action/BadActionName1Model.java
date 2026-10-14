package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class BadActionName1Model
{
  @Action( name = "assert" )
  public void setField()
  {
  }
}
