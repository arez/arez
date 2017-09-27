package com.example.on_deps_updated;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@ArezComponent
public class OnDepsUpdatedDuplicatedModel
{
  @Tracked
  public void render()
  {
  }

  @OnDepsUpdated
  public void onRenderDepsUpdated()
  {
  }

  @OnDepsUpdated( name = "render" )
  public void onRenderDepsUpdated2()
  {
  }
}
