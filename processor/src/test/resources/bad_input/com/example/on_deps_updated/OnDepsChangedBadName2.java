package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public abstract class OnDepsChangedBadName2
{
  @Track
  public void render()
  {
  }

  @OnDepsChanged( name = "class" )
  void onRenderDepsChanged()
  {
  }
}
