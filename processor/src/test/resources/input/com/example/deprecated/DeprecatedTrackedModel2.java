package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Track;

@ArezComponent
public abstract class DeprecatedTrackedModel2
{
  @Track
  public void render( final long time, float someOtherParameter )
  {
  }

  @Deprecated
  public void onRenderDepsChanged()
  {
  }
}
