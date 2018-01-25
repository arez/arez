package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public abstract class ProtectedAccessTrackedModel
{
  @Track
  protected void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  protected final void onRenderDepsChanged()
  {
  }
}
