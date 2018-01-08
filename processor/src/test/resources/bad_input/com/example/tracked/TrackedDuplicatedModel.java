package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public class TrackedDuplicatedModel
{
  @Track
  public void render()
  {
  }

  @Track( name = "render" )
  public void render2()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
