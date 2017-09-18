package com.example.action;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;

@Container
public class FunctionActionThrowsRuntimeExceptionModel
{
  @Action
  public int doStuff( final long time )
    throws RuntimeException
  {
    return 0;
  }
}
