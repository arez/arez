package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public abstract class OnDepsChangedNotAbstractModel
{
  @Track
  public void render()
  {
  }

  @OnDepsChanged
  abstract void onRenderDepsChanged();
}
