package com.example.tracked;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsChanged;
import org.realityforge.arez.annotations.Track;

@ArezComponent
public class TrackedFinalModel
{
  @Track
  public final void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
