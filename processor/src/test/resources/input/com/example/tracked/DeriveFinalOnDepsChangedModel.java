package com.example.tracked;

import arez.annotations.ArezComponent;
import arez.annotations.Track;

@ArezComponent
public abstract class DeriveFinalOnDepsChangedModel
{
  @Track
  public void render( final long time, float someOtherParameter )
  {
  }

  final void onRenderDepsChanged()
  {
  }
}
