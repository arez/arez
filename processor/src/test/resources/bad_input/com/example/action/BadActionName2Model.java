package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public class BadActionName2Model
{
  @Action( name = "ace-" )
  public void setField()
  {
  }
}
