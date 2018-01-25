package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class DeprecatedTrackedModel4
{
  public void render( final long time, float someOtherParameter )
  {
  }

  @Deprecated
  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
