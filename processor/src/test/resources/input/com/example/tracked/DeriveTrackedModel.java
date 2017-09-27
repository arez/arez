package com.example.tracked;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDepsUpdated;

@ArezComponent
public class DeriveTrackedModel
{
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsUpdated
  public void onRenderDepsUpdated()
  {
  }
}
