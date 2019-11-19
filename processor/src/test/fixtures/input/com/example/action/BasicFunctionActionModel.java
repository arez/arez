package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
abstract class BasicFunctionActionModel
{
  @Action
  public int doStuff( final long time )
  {
    return 0;
  }
}
