package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public abstract class OnDepsChangedThrowsExceptionModel
{
  @Track
  public void render()
  {
  }

  @OnDepsChanged
  void onRenderDepsChanged()
    throws Exception
  {
  }
}
