package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;

@ArezComponent
public class DeriveTrackedModel
{
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
