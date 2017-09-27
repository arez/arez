package com.example.action;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

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
