package com.example.action;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@ArezComponent
public class BasicFunctionActionModel
{
  @Action
  public int doStuff( final long time )
  {
    return 0;
  }
}
