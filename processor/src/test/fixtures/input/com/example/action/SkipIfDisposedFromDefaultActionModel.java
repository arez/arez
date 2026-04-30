package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( defaultSkipIfDisposed = Feature.ENABLE )
public abstract class SkipIfDisposedFromDefaultActionModel
{
  @Action
  public void doStuff( final long time )
  {
  }
}
