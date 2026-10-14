package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class SkipIfDisposedReturnsValueModel
{
  @Action( skipIfDisposed = Feature.ENABLE )
  public int doStuff()
  {
    return 0;
  }
}
