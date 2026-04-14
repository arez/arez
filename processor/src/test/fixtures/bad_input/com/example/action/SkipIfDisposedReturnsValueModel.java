package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class SkipIfDisposedReturnsValueModel
{
  @Action( skipIfDisposed = true )
  public int doStuff()
  {
    return 0;
  }
}
