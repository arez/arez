package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public class BasicTrackedModel
{
  @Track
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
