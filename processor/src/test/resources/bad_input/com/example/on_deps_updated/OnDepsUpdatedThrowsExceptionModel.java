package com.example.on_deps_updated;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Track;

@ArezComponent
public class OnDepsUpdatedThrowsExceptionModel
{
  @Track
  public void render()
  {
  }

  @OnDepsUpdated
  void onRenderDepsUpdated()
    throws Exception
  {
  }
}
