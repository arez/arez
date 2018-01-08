package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public class FunctionActionThrowsThrowableModel
{
  @Action
  public int doStuff( final long time )
    throws Throwable
  {
    return 0;
  }
}
