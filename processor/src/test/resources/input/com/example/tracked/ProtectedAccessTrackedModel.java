package com.example.tracked;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Track;

@ArezComponent
public class ProtectedAccessTrackedModel
{
  @Track
  protected void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsUpdated
  protected final void onRenderDepsUpdated()
  {
  }
}
