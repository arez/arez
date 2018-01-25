package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public abstract class OnDepsChangedBadName
{
  @Track
  public void render()
  {
  }

  @OnDepsChanged( name = "-ace" )
  void onRenderDepsChanged()
  {
  }
}
