package com.example.action;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

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
