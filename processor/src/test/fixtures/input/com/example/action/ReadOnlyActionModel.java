package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
abstract class ReadOnlyActionModel
{
  @Action( mutation = false )
  public int queryStuff( final long time )
  {
    return 0;
  }
}
