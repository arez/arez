package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;

@ArezComponent
public class DeprecatedTrackedModel3
{
  @Deprecated
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
