package com.example.on_deps_updated;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsChanged;
import org.realityforge.arez.annotations.Track;

@ArezComponent
public class OnDepsChangedNotPrivateModel
{
  @Track
  public void render()
  {
  }

  @OnDepsChanged
  private void onRenderDepsChanged()
  {
  }
}
