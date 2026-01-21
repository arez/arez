package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class SkipIfDisposedActionModel
{
  @Action( skipIfDisposed = true )
  public void doStuff( final long time )
  {
  }
}
