package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Track;

@ArezComponent
public abstract class DeprecatedTrackedModel1
{
  @Track
  @Deprecated
  public void render( final long time, float someOtherParameter )
  {
  }

  public void onRenderDepsChanged()
  {
  }
}
