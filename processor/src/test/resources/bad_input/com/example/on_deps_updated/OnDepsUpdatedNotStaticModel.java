package com.example.on_deps_updated;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@ArezComponent
public class OnDepsUpdatedNotStaticModel
{
  @Tracked
  public void render()
  {
  }

  @OnDepsUpdated
  static void onRenderDepsUpdated()
  {
  }
}
