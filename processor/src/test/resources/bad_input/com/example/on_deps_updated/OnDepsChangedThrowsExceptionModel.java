package com.example.on_deps_updated;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsChanged;
import org.realityforge.arez.annotations.Track;

@ArezComponent
public class OnDepsChangedThrowsExceptionModel
{
  @Track
  public void render()
  {
  }

  @OnDepsChanged
  void onRenderDepsChanged()
    throws Exception
  {
  }
}
