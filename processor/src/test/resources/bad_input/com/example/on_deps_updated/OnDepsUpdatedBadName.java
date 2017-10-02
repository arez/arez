package com.example.on_deps_updated;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Track;

@ArezComponent
public class OnDepsUpdatedBadName
{
  @Track
  public void render()
  {
  }

  @OnDepsUpdated( name = "-ace" )
  void onRenderDepsUpdated()
  {
  }
}
