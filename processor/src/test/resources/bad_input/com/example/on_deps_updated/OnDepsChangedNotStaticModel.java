package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public class OnDepsChangedNotStaticModel
{
  @Track
  public void render()
  {
  }

  @OnDepsChanged
  static void onRenderDepsChanged()
  {
  }
}
