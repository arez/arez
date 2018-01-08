package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public class UnsafeFunctionActionModel
{
  @Action
  public int doStuff( final long time )
    throws Exception
  {
    return 0;
  }
}
