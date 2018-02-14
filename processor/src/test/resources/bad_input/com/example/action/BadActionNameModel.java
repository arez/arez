package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class BadActionNameModel
{
  @Action( name = "assert" )
  public void setField()
  {
  }
}
