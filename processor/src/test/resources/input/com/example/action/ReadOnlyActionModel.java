package com.example.action;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@ArezComponent
public class ReadOnlyActionModel
{
  @Action( mutation = false )
  public int queryStuff( final long time )
  {
    return 0;
  }
}
