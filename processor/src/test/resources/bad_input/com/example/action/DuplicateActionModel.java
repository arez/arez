package com.example.action;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@ArezComponent
public class DuplicateActionModel
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
