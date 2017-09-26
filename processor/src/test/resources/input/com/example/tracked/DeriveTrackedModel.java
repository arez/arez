package com.example.tracked;

import org.realityforge.arez.annotations.Container;
import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Tracked;

@Container
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
