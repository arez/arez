package com.example.tracked;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsChanged;
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

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
