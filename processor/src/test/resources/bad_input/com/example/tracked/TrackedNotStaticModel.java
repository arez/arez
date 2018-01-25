package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public abstract class TrackedNotStaticModel
{
  @Track
  static void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
