package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public class OnDepsChangedNotPrivateModel
{
  @Track
  public void render()
  {
  }

  @OnDepsChanged
  private void onRenderDepsChanged()
  {
  }
}
