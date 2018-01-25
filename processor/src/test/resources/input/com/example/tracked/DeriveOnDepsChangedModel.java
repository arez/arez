package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.Track;

@ArezComponent
public abstract class DeriveOnDepsChangedModel
{
  @Track
  public void render( final long time, float someOtherParameter )
  {
  }

  public void onRenderDepsChanged()
  {
  }
}
