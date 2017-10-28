package com.example.tracked.other;

import org.realityforge.arez.annotations.OnDepsChanged;
import org.realityforge.arez.annotations.Track;

public class BaseModelProtectedAccess
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
