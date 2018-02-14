package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public abstract class TrackedDuplicatedName
{
  @Track
  void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }

  @Computed( name = "render" )
  int render2()
  {
    return 0;
  }
}
