package com.example.tracked;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Track;

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

  @OnDepsUpdated
  public void onRenderDepsUpdated()
  {
  }
}
