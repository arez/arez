package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( defaultSkipIfDisposed = Feature.ENABLE )
public abstract class SkipIfDisposedDisabledOverrideActionModel
{
  @Action( skipIfDisposed = Feature.DISABLE )
  public void doStuff( final long time )
  {
  }
}
