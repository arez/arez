package com.example.tracked.other;

import org.realityforge.arez.annotations.OnDepsUpdated;
import org.realityforge.arez.annotations.Track;

public class BaseModelProtectedAccess
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
