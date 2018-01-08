package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public class OnDepsChangedDuplicatedModel
{
  @Track
  public void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }

  @OnDepsChanged( name = "render" )
  public void onRenderDepsChanged2()
  {
  }
}
