package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public abstract class TrackedAbstractModel
{
  @Track
  abstract void render();

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
