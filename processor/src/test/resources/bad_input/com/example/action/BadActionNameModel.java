package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public class BadActionNameModel
{
  @Action( name = "-ace" )
  public void setField()
  {
  }
}
