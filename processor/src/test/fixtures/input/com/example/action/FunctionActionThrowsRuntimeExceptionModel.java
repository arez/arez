package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
abstract class FunctionActionThrowsRuntimeExceptionModel
{
  @Action
  public int doStuff( final long time )
    throws RuntimeException
  {
    return 0;
  }
}
