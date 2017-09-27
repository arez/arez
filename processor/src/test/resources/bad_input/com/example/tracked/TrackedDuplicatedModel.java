package com.example.tracked;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@ArezComponent
public class TrackedDuplicatedModel
{
  @Tracked
  public void render()
  {
  }

  @Tracked( name = "render" )
  public void render2()
  {
  }

  @OnDepsUpdated
  public void onRenderDepsUpdated()
  {
  }
}
