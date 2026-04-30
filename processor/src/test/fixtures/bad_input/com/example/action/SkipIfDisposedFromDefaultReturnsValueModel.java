package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( defaultSkipIfDisposed = Feature.ENABLE )
public abstract class SkipIfDisposedFromDefaultReturnsValueModel
{
  @Action
  public int doStuff()
  {
    return 0;
  }
}
