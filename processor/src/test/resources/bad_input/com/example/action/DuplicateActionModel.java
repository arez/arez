package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class DuplicateActionModel
{
  @Action( name = "ace" )
  public void setField()
  {
  }

  @Action( name = "ace" )
  public void setField2()
  {
  }
}
